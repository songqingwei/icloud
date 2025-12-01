package cn.isqing.icloud.starter.admin.service.mfa;

import cn.isqing.icloud.common.api.dto.Response;

public interface MfaService {
    /**
     * 为用户生成MFA密钥和二维码
     * @param userId 用户ID
     * @return 包含二维码URL的响应
     */
    Response<String> generateMfaQRCode(Long userId);

    /**
     * 启用用户的MFA
     * @param userId 用户ID
     * @param code 用户输入的验证码
     * @return 是否启用成功
     */
    Response<Boolean> enableMfa(Long userId, String code);

    /**
     * 验证MFA验证码
     * @param userId 用户ID
     * @param code 用户输入的验证码
     * @return 验证是否成功
     */
    Response<Boolean> verifyMfaCode(Long userId, String code);

    /**
     * 检查用户是否启用了MFA
     * @param userId 用户ID
     * @return 是否启用MFA
     */
    Response<Boolean> isMfaEnabled(Long userId);
}