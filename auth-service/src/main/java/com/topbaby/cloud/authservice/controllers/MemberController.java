package com.topbaby.cloud.authservice.controllers;

import com.topbaby.cloud.authservice.client.CommonServiceClient;
import com.topbaby.cloud.authservice.entity.MemberCredentialsDO;
import com.topbaby.cloud.authservice.entity.dto.SendMsgDTO;
import com.topbaby.cloud.authservice.service.MemberCredentialsService;
import com.topbaby.cloud.authservice.util.Constants;
import com.topbaby.cloud.authservice.util.DateUtil;
import com.topbaby.cloud.authservice.util.RedisConstants;
import com.topbaby.cloud.authservice.util.UserAgentUtil;
import com.topbaby.data.model.MessageDTO;
import com.topbaby.data.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>会员相关 接口</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@RestController
public class MemberController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberCredentialsService memberCredentialsService;
    @Autowired
    private CommonServiceClient commonClient;

    /**
     * 发送短信验证码
     *
     * @param request HttpServletRequest
     * @param phone   手机号
     * @return MessageDTO
     */
    @GetMapping("/message/code")
    public MessageDTO messageCode(HttpServletRequest request, String phone) {

        // TODO: 18-3-23 手机号是否合法
        /*if(!MessageType.SUCCESS.getValue().equals(commonClient.checkPhoneNum(phone).getType())){
            return new MessageDTO(MessageType.ERROR, "手机号码不合法！");
        }*/

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("credential", phone);

        String flag = null;
        MemberCredentialsDO memberCredential = memberCredentialsService.getOne(params);
        if (memberCredential == null) {
            flag = Constants.SEND_REGISTER_CODE;
        } else {
            flag = Constants.SEND_MOBILE_LOGIN_CODE;
        }

        SendMsgDTO sendEntity = new SendMsgDTO();
        sendEntity.setCredential(phone);
        return handleMessageCode(request, sendEntity, flag);
    }

    /**
     * 生成短信验证码并发送
     *
     * @param request    HttpServletRequest
     * @param sendEntity 短信信息
     * @param flag       发送验证码类型
     * @return MessageDTO
     */
    public MessageDTO handleMessageCode(HttpServletRequest request, SendMsgDTO sendEntity, String flag) {

        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        Object smsSendFrequencyObj = valueOps.get(RedisConstants.SMS_SEND_FREQUENCY_PREFIX + sendEntity.getCredential());
        if (null == smsSendFrequencyObj) {
            valueOps.set(RedisConstants.SMS_SEND_FREQUENCY_PREFIX + sendEntity.getCredential(), 1, DateUtil.getTodayRemainingMillisecond(), TimeUnit.MILLISECONDS);
        } else {
            int smsSendFrequency = Integer.parseInt(smsSendFrequencyObj.toString());
            if (smsSendFrequency >= RedisConstants.SMS_SEND_MAX_FREQUENCY_ONE_DAY) {
                List<String> errorList = new ArrayList<String>();
                errorList.add("max");
                return new MessageDTO(MessageType.ERROR, "当天短信验证码发送已达上线！", errorList);
            }

            if (smsSendFrequency >= RedisConstants.SMS_SEND_THRESHOLD_FOR_SHOW_VERIFICATION_CODE) {
                if (null == valueOps.get(RedisConstants.SMS_FOR_SHOW_VERIFICATION_CODE_FLAG_PREFIX + sendEntity.getCredential())) {
                    List<String> errorList = new ArrayList<String>();
                    errorList.add("captcha");
                    return new MessageDTO(MessageType.ERROR, "短信验证码发送已达3次，显示图形验证码！", errorList);
                }
                valueOps.set(RedisConstants.SMS_FOR_SHOW_VERIFICATION_CODE_FLAG_PREFIX + sendEntity.getCredential(), null);
            }

            valueOps.set(RedisConstants.SMS_SEND_FREQUENCY_PREFIX + sendEntity.getCredential(), smsSendFrequency + 1, DateUtil.getTodayRemainingMillisecond(), TimeUnit.MILLISECONDS);
        }


        //动态码生成
        String messageCode = String.valueOf((int) (Math.random() * 9000 + 1000));
        // 设置30分钟失效
        valueOps.set(RedisConstants.PHONE_MESSAGE_CODE_PREFIX + sendEntity.getCredential(), messageCode, 30, TimeUnit.MINUTES);
        String credentialType = "mobile";

        if (flag.equals(Constants.SEND_REGISTER_CODE)) {
            String message = "【淘璞】您本次注册的验证码为:" + messageCode;
            sendEntity.setMessage(message);
            sendEntity.setSendWay(credentialType);
        } else if (flag.equals(Constants.SEND_MOBILE_LOGIN_CODE)) {
            String message = "【淘璞】您本次登录的验证码为:" + messageCode;
            sendEntity.setMessage(message);
            sendEntity.setSendWay(credentialType);
        }

        MessageDTO result = null;
        sendEntity.setType(Constants.SEND_OPTION_CAPTCHA_TYPE);
        sendEntity.setIp(UserAgentUtil.getRemoteAddress(request));
        // TODO: 18-3-23  调用公用服务
        result = commonClient.sendMessageCode(sendEntity);

        logger.info("动态码为: " + messageCode);
        if (result != null && MessageType.SUCCESS.getValue().equals(result.getType())) {
            return new MessageDTO(MessageType.SUCCESS, "验证码发送成功");
        } else {
            return new MessageDTO(MessageType.ERROR, "验证码发送失败，请检查网络稍后重试");
        }
    }
}
