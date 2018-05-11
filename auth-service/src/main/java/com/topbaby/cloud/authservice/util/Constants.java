package com.topbaby.cloud.authservice.util;

/**
 * <p>常量类</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
public class Constants {
    // 短信验证码发送成功
    public static final String SEND_RESULT_STATUS_SUCCESS = "1";
    // 短信验证码发送失败
    public static final String SEND_RESULT_STATUS_ERROR = "0";
    // 短信验证码60秒重复发送
    public static final String SEND_RESULT_STATUS_REPEAT_TIMES = "2";
    // 短信 验证码达到失败上线 需要填写图像验证
    public static final String SEND_RESULT_STATUS_NEED_CAPTCHA = "5";
    // 短信验证码单ip24小时超过20条，单用户超过5条
    public static final String SEND_RESULT_STATUS_REPEAT_IP = "3";
    // 短信发送类型‘1’为注册、找回密码获取类型
    public static final String SEND_OPTION_CAPTCHA_TYPE = "1";

    public static final String SEND_MOBILE_LOGIN_CODE = "send_mobile_login_code";
    public static final String SEND_PAYMENT_CODE = "send_payment_code";
    public static final String SEND_REGISTER_CODE = "send_register_code";
    public static final String SEND_FIND_PWD_CODE = "send_find_pwd_code";
    public static final String SEND_FAIL_TIMES = "send_fail_times";
}
