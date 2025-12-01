package cn.isqing.icloud.starter.admin.service.auth;

import cn.isqing.icloud.starter.admin.common.constants.AdminConstants;
import cn.isqing.icloud.starter.admin.dao.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AdminUserDetails implements UserDetails {
    private User user;

    public AdminUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // admin用户拥有所有权限
        if (AdminConstants.DEFAULT_ADMIN_USERNAME.equals(user.getUsername())) {
            // 可以根据需要添加更多权限
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        // 其他用户简单起见，给所有用户分配 ADMIN 权限
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getStatus() == 1; // 1表示正常状态
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1; // 1表示正常状态
    }

    public User getUser() {
        return user;
    }
}