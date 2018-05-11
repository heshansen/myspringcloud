package com.topbaby.cloud.authservice.client.fallback;

import com.topbaby.cloud.authservice.client.CommonServiceClient;
import com.topbaby.cloud.authservice.entity.dto.SendMsgDTO;
import com.topbaby.data.model.MessageDTO;
import com.topbaby.data.model.MessageType;
import org.springframework.stereotype.Component;

/**
 * <p>common-service fallback</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@Component
public class CommonServiceFallback implements CommonServiceClient {
    @Override
    public MessageDTO sendMessageCode(SendMsgDTO sendMsgDTO) {
        return new MessageDTO(MessageType.ERROR, "发送失败，请稍后重试！");
    }

    @Override
    public MessageDTO checkPhoneNum(String phone) {
        return new MessageDTO(MessageType.ERROR, "校验失败！");
    }
}
