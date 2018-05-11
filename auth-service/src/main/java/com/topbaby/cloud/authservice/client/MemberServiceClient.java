package com.topbaby.cloud.authservice.client;

import com.topbaby.cloud.authservice.client.fallback.MemberServiceFallBack;
import com.topbaby.cloud.authservice.config.FeignOAuthConfig;
import com.topbaby.cloud.authservice.entity.MemberDO;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>member-service 客户端</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
@FeignClient(name = "member-service", url = "http://localhost:8083", fallback = MemberServiceFallBack.class, configuration = FeignOAuthConfig.class)
public interface MemberServiceClient {

    /**
     * 会员注册
     *
     * @param credential      手机号码
     * @param password        密码
     * @param brandshopUserId 导购员id
     * @param brandshopId     异业联盟门店id
     * @return MemberDO
     */
    @RequestMapping(method = RequestMethod.GET, value = "/member/user/register")
    MemberDO register(@RequestParam("credential")String credential, @RequestParam("password")String password, @RequestParam("brandshopUserId")Long brandshopUserId, @RequestParam("brandshopId")Long brandshopId);

}
