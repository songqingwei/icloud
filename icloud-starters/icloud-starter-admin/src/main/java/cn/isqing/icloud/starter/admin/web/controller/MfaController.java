package cn.isqing.icloud.starter.admin.web.controller;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.MfaVerifyRequestDto;
import cn.isqing.icloud.starter.admin.service.mfa.MfaService;
import cn.isqing.icloud.starter.admin.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/mfa")
public class MfaController {

    @Autowired
    private UserService userService;

    /**
     * 生成MFA二维码
     */
    @PostMapping("/generate")
    public Response<String> generateMfaQRCode(@RequestParam Long userId) {
        return userService.generateMfaQRCode(userId);
    }

    /**
     * 启用MFA
     */
    @PostMapping("/enable")
    public Response<Boolean> enableMfa(@RequestBody MfaVerifyRequestDto reqDto) {
        return userService.enableMfa(reqDto);
    }

    /**
     * 验证MFA验证码
     */
    @PostMapping("/verify")
    public Response<Boolean> verifyMfaCode(@RequestBody MfaVerifyRequestDto reqDto) {
        return userService.verifyMfaCode(reqDto);
    }
}