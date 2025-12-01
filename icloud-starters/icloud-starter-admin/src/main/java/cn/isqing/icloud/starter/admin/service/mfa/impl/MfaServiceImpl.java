package cn.isqing.icloud.starter.admin.service.mfa.impl;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.dao.entity.User;
import cn.isqing.icloud.starter.admin.dao.entity.UserMfa;
import cn.isqing.icloud.starter.admin.dao.mapper.UserMapper;
import cn.isqing.icloud.starter.admin.dao.mapper.UserMfaMapper;
import cn.isqing.icloud.starter.admin.service.mfa.MfaService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@Slf4j
public class MfaServiceImpl implements MfaService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMfaMapper userMfaMapper;

    @Override
    public Response<String> generateMfaQRCode(Long userId) {
        try {
            // 获取用户信息
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Response.error("用户不存在");
            }

            // 生成密钥
            SecretGenerator secretGenerator = new DefaultSecretGenerator();
            String secret = secretGenerator.generate();

            // 生成二维码数据
            QrData data = new QrData.Builder()
                    .label(user.getUsername() + "@iCloud")
                    .secret(secret)
                    .issuer("iCloud Admin")
                    .algorithm(HashingAlgorithm.SHA1)
                    .digits(6)
                    .period(30)
                    .build();

            // 生成二维码图片
            QrGenerator qrGenerator = new ZxingPngQrGenerator();
            byte[] imageData = qrGenerator.generate(data);
            String qrCodeImageUri = getDataUriForImage(imageData, qrGenerator.getImageMimeType());

            // 保存密钥到数据库（未启用状态）
            UserMfa userMfa = new UserMfa();
            userMfa.setUserId(userId);
            userMfa.setSecretKey(secret);
            userMfa.setIsEnabled(0); // 未启用
            userMfa.setCreateTime(LocalDateTime.now());
            userMfa.setUpdateTime(LocalDateTime.now());
            
            UserMfa existingMfa = userMfaMapper.selectByUserId(userId);
            if (existingMfa != null) {
                userMfaMapper.update(userMfa);
            } else {
                userMfaMapper.insert(userMfa);
            }

            return Response.success(qrCodeImageUri);
        } catch (QrGenerationException e) {
            log.error("生成MFA二维码失败", e);
            return Response.error("生成二维码失败");
        }
    }

    @Override
    public Response<Boolean> enableMfa(Long userId, String code) {
        UserMfa userMfa = userMfaMapper.selectByUserId(userId);
        if (userMfa == null) {
            return Response.error("用户未生成MFA密钥");
        }

        // 验证代码是否正确
        TimeProvider timeProvider = new SystemTimeProvider();
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        
        boolean isValid = verifier.isValidCode(userMfa.getSecretKey(), code);
        if (!isValid) {
            return Response.error("验证码不正确");
        }

        // 启用MFA
        userMfa.setIsEnabled(1);
        userMfa.setUpdateTime(LocalDateTime.now());
        userMfaMapper.update(userMfa);

        return Response.success(true);
    }

    @Override
    public Response<Boolean> verifyMfaCode(Long userId, String code) {
        UserMfa userMfa = userMfaMapper.selectByUserId(userId);
        if (userMfa == null || userMfa.getIsEnabled() != 1) {
            // 如果用户没有启用MFA，则直接返回成功
            return Response.success(true);
        }

        TimeProvider timeProvider = new SystemTimeProvider();
        DefaultCodeGenerator codeGenerator = new DefaultCodeGenerator(HashingAlgorithm.SHA1, 6);
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        
        boolean isValid = verifier.isValidCode(userMfa.getSecretKey(), code);
        if (!isValid) {
            return Response.error("验证码不正确");
        }

        return Response.success(true);
    }

    @Override
    public Response<Boolean> isMfaEnabled(Long userId) {
        UserMfa userMfa = userMfaMapper.selectByUserId(userId);
        if (userMfa == null) {
            return Response.success(false);
        }
        return Response.success(userMfa.getIsEnabled() == 1);
    }
}