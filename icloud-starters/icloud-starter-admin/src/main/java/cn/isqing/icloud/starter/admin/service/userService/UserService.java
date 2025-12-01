package cn.isqing.icloud.starter.admin.service.userService;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.MfaVerifyRequestDto;
import cn.isqing.icloud.starter.admin.api.dto.UserDto;
import cn.isqing.icloud.starter.admin.api.dto.UserLoginRequestDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    Response<String> login(UserLoginRequestDto reqDto);

    Response<Boolean> register(UserDto userDto);

    Response<UserDto> getUserById(Long userId);

    Response<Boolean> updateUser(UserDto userDto);

    Response<Boolean> deleteUser(Long userId);

    Response<PageResDto<UserDto>> listUsers(PageReqDto<UserDto> req);

    Response<List<UserDto>> getUserRoles(Long userId);

    Response<Boolean> assignUserRole(Long userId, Set<Long> roleIds);
    
    /**
     * 生成MFA二维码
     *
     * @param userId 用户ID
     * @return 二维码图片URL
     */
    Response<String> generateMfaQRCode(Long userId);

    /**
     * 启用MFA
     *
     * @param reqDto MFA启用请求参数
     * @return 启用结果
     */
    Response<Boolean> enableMfa(MfaVerifyRequestDto reqDto);

    /**
     * 验证MFA验证码
     *
     * @param reqDto MFA验证请求参数
     * @return 验证结果
     */
    Response<Boolean> verifyMfaCode(MfaVerifyRequestDto reqDto);
}