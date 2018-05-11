package com.topbaby.cloud.authservice.enums;

/**
 * <p>账户 appid 枚举类</p>
 *
 * @author SunXiaoYuan
 * @date 17-12-21
 * @since 1.0.0
 */
public enum UserAppid {

    MOS("商户系统账号", "mos"),
    OMS("平台系统账号", "oms"),
    BSS("门店系统账号", "bss");

    private final String name;
    private final String value;

    UserAppid(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static String getName(String value) {
        for (UserAppid status : UserAppid.values()) {
            if (status.getValue().equals(value)) {
                return status.getName();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
