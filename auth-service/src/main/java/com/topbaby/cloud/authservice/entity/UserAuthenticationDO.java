package com.topbaby.cloud.authservice.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * <p>用户登录凭证 DO</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
public class UserAuthenticationDO implements UserDetails {
    private Long userId;
    private String username;
    private String password;
    private final Set<GrantedAuthority> authorities;
    private final Boolean accountNonExpired = true;
    private final Boolean accountNonLocked = true;
    private final Boolean credentialsNonExpired = true;
    private final Boolean enabled = true;

    public UserAuthenticationDO(String username, String password, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public UserAuthenticationDO(String username, String password, Long userId, Set<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.authorities = authorities;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
