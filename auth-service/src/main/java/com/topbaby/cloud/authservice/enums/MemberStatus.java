package com.topbaby.cloud.authservice.enums;

/**
 * <p>会员状态</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
public enum MemberStatus {

    NORMAL("正常", "N"),
    CANCEL("销户", "C"),
    LOCK("锁定", "L"),
    UNACTIVE("待激活", "U");

    private final String name;
    private final String value;

    MemberStatus(String name, String value) {
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
        for (MemberStatus temp : MemberStatus.values()) {
            if (temp.getValue().equals(value)) {
                return temp.getName();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
