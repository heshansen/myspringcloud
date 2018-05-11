package com.topbaby.cloud.authservice.client;

import com.topbaby.cloud.authservice.client.fallback.CommonServiceFallback;
import com.topbaby.cloud.authservice.config.FeignOAuthConfig;
import com.topbaby.cloud.authservice.entity.dto.SendMsgDTO;
import com.topbaby.data.model.MessageDTO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>common-service 客户端</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@FeignClient(name = "common-service", url = "http://localhost:8083", fallback = CommonServiceFallback.class, configuration = FeignOAuthConfig.class)
public interface CommonServiceClient {

    /**
     * 调用外部服务发送短信接口
     *
     * @param sendMsgDTO 短信信息实体
     * @return MessageDTO
     */
    @RequestMapping(method = RequestMethod.POST, value = "/common/send/sms/msg", consumes = "application/json")
    MessageDTO sendMessageCode(SendMsgDTO sendMsgDTO);

    /**
     * 验证手机号段是否合法
     *
     * @param phone 手机号
     * @return MessageDTO
     */
    @RequestMapping(method = RequestMethod.GET, value = "/common/phone-manager/checker")
    MessageDTO checkPhoneNum(@RequestParam("phone")String phone);

}
