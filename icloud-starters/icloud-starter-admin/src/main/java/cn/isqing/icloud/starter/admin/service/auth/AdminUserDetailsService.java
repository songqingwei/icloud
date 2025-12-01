package cn.isqing.icloud.starter.admin.service.auth;

import cn.isqing.icloud.starter.admin.common.constants.AdminConstants;
import cn.isqing.icloud.starter.admin.dao.entity.User;
import cn.isqing.icloud.starter.admin.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;

@Slf4j
@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Value("${" + AdminConstants.ADMIN_PASSWORD + ":admin}")
    private String defaultPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库查找用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 如果数据库中的密码为空，则使用默认密码
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            log.info("用户 {} 密码为空，使用默认密码进行认证", username);
            user.setPassword(passwordEncoder.encode(defaultPassword));
        }

        return new AdminUserDetails(user);
    }
}