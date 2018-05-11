package com.topbaby.cloud.authservice;

import com.topbaby.cloud.authservice.entity.UserAuthenticationDO;
import com.topbaby.cloud.authservice.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;


public class UserTokenEnhancer implements TokenEnhancer {

    private static final Log logger = LogFactory.getLog(UserTokenEnhancer.class);

    @Autowired
    private UserService userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> userAdditionalInfo = new HashMap<>();

        Long userSeq = ((UserAuthenticationDO) authentication.getUserAuthentication().getPrincipal()).getUserId();

        userAdditionalInfo.put("userSeq", String.valueOf(userSeq));

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(userAdditionalInfo);

        return accessToken;
    }
}
