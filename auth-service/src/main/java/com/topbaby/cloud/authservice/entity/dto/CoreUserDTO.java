package com.topbaby.cloud.authservice.entity.dto;

/**
 * <p>基础用户信息 DTO</p>
 *
 * @author SunXiaoYuan
 * @date 17-11-30
 * @since 1.0.0
 */
public class CoreUserDTO {
    /**
     * 初次登录标识：0-正常，1-初次登录
     */
    private Integer firstLogin;

    /**
     * 用户密码
     */
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(Integer firstLogin) {
        this.firstLogin = firstLogin;
    }

    @Override
    public String toString() {
        return "CoreUserDTO{" +
                                          "firstLogin=" + firstLogin +
                                          ", password='" + password + '\'' +
                                          '}';
    }
}
