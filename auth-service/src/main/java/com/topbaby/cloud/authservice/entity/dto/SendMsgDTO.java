package com.topbaby.cloud.authservice.entity.dto;

/**
 * <p>发送信息对象</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
public class SendMsgDTO {
    /**
     * 手机号
     */
    private String credential;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 发送类型
     * '1'==验证码；
     * ‘2’==订单处理（根据后面业务细化）
     * 3 订单已支付、
     * 4取消已支付订单(取消中、已完成取消) 、
     * 5退货（退货中，退货已完成）、
     * 6商户已发货
     */
    private String type;
    /**
     * 发送方式 '0'==手机；'1'==邮箱
     */
    private String sendWay;
    /**
     * 发送内容
     */
    private String message;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 发送次数
     */
    private Integer sendCount;
    /**
     * 短信接收者，1 商户 2导购员 3会员
     */
    private String receiver;

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendWay() {
        return sendWay;
    }

    public void setSendWay(String sendWay) {
        this.sendWay = sendWay;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
