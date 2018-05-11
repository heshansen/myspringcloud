package com.topbaby.cloud.authservice.service.provider;

import com.topbaby.cloud.authservice.client.MemberServiceClient;
import com.topbaby.cloud.authservice.service.MemberService;
import com.topbaby.cloud.authservice.service.UserService;
import com.topbaby.cloud.authservice.util.RedisConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>自定义 AuthenticationProvider</p>
 * 根据不同的登录类型，执行不同的登录认证策略
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
public class TopAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private Log logger = LogFactory.getLog(getClass());

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    // ~ Instance fields
    // ================================================================================================

    private PasswordEncoder passwordEncoder;

    private String userNotFoundEncodedPassword;

    private SaltSource saltSource;

    private UserService userService;

    private MemberService memberService;

    private RedisTemplate redisTemplate;

    public TopAuthenticationProvider() {
        setPasswordEncoder(new PlaintextPasswordEncoder());
    }

    @Override
    protected void doAfterPropertiesSet() throws Exception {

    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        Map<String, String> parameters = (Map<String, String>) authentication.getDetails();
        String paramName = "type";

        String type = parameters.get(paramName);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ("topBssClient".equals(user.getUsername())) {
            handlePasswordChecks(userDetails, authentication);
        } else if ("topCosClient".equals(user.getUsername()) && "mobile".equals(type)) {
            logger.info("====== CustomAuthenticationProvider 手机验证码校验");
            String messageCode = parameters.get("code");
            handleMessageCodeChecks(userDetails.getUsername(), messageCode);
        } else if ("topCosClient".equals(user.getUsername()) && "password".equals(type)) {
            logger.info("====== CustomAuthenticationProvider 密码校验");
            handlePasswordChecks(userDetails, authentication);
        } else {
            throw new BadCredentialsException(messages.getMessage(
                                              "TopAuthenticationProvider.badCredentials",
                                              "Bad clientIds"));
        }
    }

    /**
     * TODO
     * 短信验证码校验
     *
     * @param phone       手机号
     * @param messageCode 验证码
     */
    private void handleMessageCodeChecks(String phone, String messageCode) throws AuthenticationException {
        if (messageCode == null) {
            throw new BadCredentialsException(messages.getMessage(
                                              "TopAuthenticationProvider.badMessageCode",
                                              "MessageCode is null"));
        }
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        String expectMessageCode = (String) valueOps.get(RedisConstants.PHONE_MESSAGE_CODE_PREFIX + phone);

        if (!messageCode.equals(expectMessageCode)) {
            throw new BadCredentialsException(messages.getMessage(
                                              "TopAuthenticationProvider.badMessageCode",
                                              "Bad MessageCode"));
        }

        //TODO 待确认 验证成功后，删除该手机号关联的验证码
//        valueOps.getOperations().delete(SEND_MESSAGE_CODE_PREFIX+phone);

    }

    /**
     * 密码校验
     *
     * @param userDetails    用户信息详情
     * @param authentication 当前认证对象
     */
    private void handlePasswordChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        Object salt = null;

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                                              "AbstractUserDetailsAuthenticationProvider.badCredentials",
                                              "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!passwordEncoder.isPasswordValid(userDetails.getPassword(),
                                          presentedPassword, salt)) {
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                                              "AbstractUserDetailsAuthenticationProvider.badCredentials",
                                              "Bad credentials"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser;

        Map<String, Object> parameters = (Map<String, Object>) authentication.getDetails();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            /**
             * 根据clientId判断是由哪个客户端发来的请求
             */
            if ("topCosClient".equals(user.getUsername())) {
                loadedUser = this.memberService.getMemberByCredential(username,parameters);
            } else {
                loadedUser = this.userService.getUserByLoginId(username);
            }

        } catch (UsernameNotFoundException notFound) {
            throw notFound;
        } catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                                              repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                                              "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    public void setPasswordEncoder(Object passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        if (passwordEncoder instanceof PasswordEncoder) {
            setPasswordEncoder((PasswordEncoder) passwordEncoder);
            return;
        }

        if (passwordEncoder instanceof org.springframework.security.crypto.password.PasswordEncoder) {
            final org.springframework.security.crypto.password.PasswordEncoder delegate = (org.springframework.security.crypto.password.PasswordEncoder) passwordEncoder;
            setPasswordEncoder(new PasswordEncoder() {
                public String encodePassword(String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.encode(rawPass);
                }

                public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
                    checkSalt(salt);
                    return delegate.matches(rawPass, encPass);
                }

                private void checkSalt(Object salt) {
                    Assert.isNull(salt,
                                                      "Salt value must be null when used with crypto module PasswordEncoder");
                }
            });

            return;
        }

        throw new IllegalArgumentException(
                                          "passwordEncoder must be a PasswordEncoder instance");
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        this.userNotFoundEncodedPassword = passwordEncoder.encodePassword(
                                          USER_NOT_FOUND_PASSWORD, null);
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public MemberService getMemberService() {
        return memberService;
    }

    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }
}
