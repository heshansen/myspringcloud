package com.topbaby.cloud.authservice.client.fallback;

import com.topbaby.cloud.authservice.client.MemberServiceClient;
import com.topbaby.cloud.authservice.entity.MemberDO;
import org.springframework.stereotype.Component;

/**
 * <p>member-service fallback</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
@Component
public class MemberServiceFallBack implements MemberServiceClient {

    @Override
    public MemberDO register(String credential, String password, Long brandshopUserId, Long brandshopId) {
        return null;
    }
}
