package com.topbaby.cloud.authservice.util;

/**
 * <p>Redis相关常量类</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
public class RedisConstants {

    /**
     * 短信（手机验证码）发送次数大于3次时开关变量
     */
    public static final String SMS_FOR_SHOW_VERIFICATION_CODE_FLAG_PREFIX = "sms_for_show_verification_code_flag_prefix_";
    /**
     * 记录每个手机号短信发送次数的key(对应的value值的过期时间为当天的23:59:59)
     */
    public static final String SMS_SEND_FREQUENCY_PREFIX = "sms_send_frequency_";
    /**
     * 手机验证码存储前缀
     */
    public static final String PHONE_MESSAGE_CODE_PREFIX = "phone_message_code_prefix_";
    /**
     * 显示图形验证码的短信（手机验证码）发送次数阀值
     */
    public static final int SMS_SEND_THRESHOLD_FOR_SHOW_VERIFICATION_CODE = 2;
    /**
     * 手机验证码一天中最多发送次数
     */
    public static final int SMS_SEND_MAX_FREQUENCY_ONE_DAY = 6;
}
