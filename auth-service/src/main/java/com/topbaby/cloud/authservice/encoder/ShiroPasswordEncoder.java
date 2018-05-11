package com.topbaby.cloud.authservice.encoder;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>密码加密类</p>
 * 使用shiro提供的默认加密方法
 *
 * @author SunXiaoYuan
 * @date 17-12-4
 * @since 1.0.0
 */
public class ShiroPasswordEncoder implements PasswordEncoder {

    private DefaultPasswordService defaultPasswordService;

    public ShiroPasswordEncoder() {
        this.defaultPasswordService = new DefaultPasswordService();
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return this.defaultPasswordService.encryptPassword(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.defaultPasswordService.passwordsMatch(rawPassword, encodedPassword);
    }
}
