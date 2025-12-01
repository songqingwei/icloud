package cn.isqing.icloud.starter.admin.service.userService.impl;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.MfaVerifyRequestDto;
import cn.isqing.icloud.starter.admin.api.dto.UserDto;
import cn.isqing.icloud.starter.admin.api.dto.UserLoginRequestDto;
import cn.isqing.icloud.starter.admin.common.constants.AdminConstants;
import cn.isqing.icloud.starter.admin.dao.entity.User;
import cn.isqing.icloud.starter.admin.dao.entity.Role;
import cn.isqing.icloud.starter.admin.dao.mapper.UserMapper;
import cn.isqing.icloud.starter.admin.service.mfa.MfaService;
import cn.isqing.icloud.starter.admin.service.userService.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MfaService mfaService;

    @Override
    public Response<String> login(UserLoginRequestDto reqDto) {
        User user = userMapper.selectByUsername(reqDto.getUsername());
        if (user == null) {
            return Response.error("用户名或密码错误");
        }

        if (!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())) {
            return Response.error("用户名或密码错误");
        }

        // 检查MFA是否启用
        Response<Boolean> mfaEnabledResponse = mfaService.isMfaEnabled(user.getId());
        if (!mfaEnabledResponse.isSuccess()) {
            return Response.error("检查MFA设置时发生错误");
        }
        
        if (mfaEnabledResponse.getData()) {
            return Response.success("需要MFA验证");
        }

        // 这里应该生成JWT token，简化处理返回成功
        return Response.success("登录成功");
    }

    @Override
    public Response<Boolean> register(UserDto userDto) {
        // 检查是否尝试注册admin用户
        if (AdminConstants.DEFAULT_ADMIN_USERNAME.equals(userDto.getUsername())) {
            return Response.error("不允许注册admin用户");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return Response.success(true);
    }

    @Override
    public Response<UserDto> getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Response.error("用户不存在");
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return Response.success(userDto);
    }

    @Override
    public Response<Boolean> updateUser(UserDto userDto) {
        // 检查是否是admin用户，如果是则不允许修改
        User existingUser = userMapper.selectById(userDto.getId());
        if (existingUser != null && AdminConstants.DEFAULT_ADMIN_USERNAME.equals(existingUser.getUsername())) {
            return Response.error("admin用户不允许修改");
        }
        
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> deleteUser(Long userId) {
        // 检查是否是admin用户，如果是则不允许删除
        User user = userMapper.selectById(userId);
        if (user != null && AdminConstants.DEFAULT_ADMIN_USERNAME.equals(user.getUsername())) {
            return Response.error("admin用户不允许删除");
        }
        
        userMapper.deleteById(userId);
        return Response.success(true);
    }

    @Override
    public Response<PageResDto<UserDto>> listUsers(PageReqDto<UserDto> req) {
        User user = new User();
        if (req.getCondition() != null) {
            BeanUtils.copyProperties(req.getCondition(), user);
        }

        PageReqDto.PageInfo pageInfo = req.getPageInfo();
        int offset = (pageInfo.getPageNum() - 1) * pageInfo.getPageSize();
        List<User> users = userMapper.selectPage(user, offset, pageInfo.getPageSize());
        int total = userMapper.count(user);

        List<UserDto> userDtos = users.stream().map(u -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(u, userDto);
            return userDto;
        }).collect(Collectors.toList());

        PageResDto<UserDto> pageResDto = new PageResDto<>();
        pageResDto.setList(userDtos);
        pageResDto.setTotal((long) total);

        return Response.success(pageResDto);
    }

    @Override
    public Response<List<UserDto>> getUserRoles(Long userId) {
        List<Role> roles = userMapper.selectRolesByUserId(userId);
        List<UserDto> roleDtos = roles.stream().map(role -> {
            UserDto roleDto = new UserDto();
            // 注意：这里需要映射Role到UserDto，但字段不完全匹配
            // 正确的做法应该是有一个RoleDto，这里为了满足接口定义暂时这样做
            roleDto.setId(role.getId());
            roleDto.setUsername(role.getName());
            return roleDto;
        }).collect(Collectors.toList());
        return Response.success(roleDtos);
    }

    @Override
    public Response<Boolean> assignUserRole(Long userId, Set<Long> roleIds) {
        // 实现分配用户角色逻辑
        return Response.success(true);
    }
    
    @Override
    public Response<String> generateMfaQRCode(Long userId) {
        return mfaService.generateMfaQRCode(userId);
    }

    @Override
    public Response<Boolean> enableMfa(MfaVerifyRequestDto reqDto) {
        return mfaService.enableMfa(reqDto.getUserId(), reqDto.getCode());
    }

    @Override
    public Response<Boolean> verifyMfaCode(MfaVerifyRequestDto reqDto) {
        return mfaService.verifyMfaCode(reqDto.getUserId(), reqDto.getCode());
    }
}