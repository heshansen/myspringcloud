package com.topbaby.cloud.authservice.config;

import feign.RequestInterceptor;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

/**
 * <p>Feign 配置类</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@Configuration
public class FeignOAuthConfig {
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(OAuth2ClientContext oAuth2ClientContext) {
        return new OAuth2FeignRequestInterceptor(oAuth2ClientContext, new AuthorizationCodeResourceDetails());
    }
}
