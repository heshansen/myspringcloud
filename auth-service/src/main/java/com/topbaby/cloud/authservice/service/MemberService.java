package com.topbaby.cloud.authservice.service;

import com.topbaby.cloud.authservice.client.MemberServiceClient;
import com.topbaby.cloud.authservice.entity.MemberDO;
import com.topbaby.cloud.authservice.entity.UserAuthenticationDO;
import com.topbaby.cloud.authservice.enums.MemberStatus;
import com.topbaby.cloud.authservice.mappers.MemberDao;
import com.topbaby.cloud.authservice.util.RedisConstants;
import com.topbaby.data.AbstractDataAccessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>会员服务Service</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-26
 * @since 1.0.0
 */
@Service
public class MemberService extends AbstractDataAccessor<MemberDO, MemberDao> {

    private final Logger logger = Logger.getLogger(this.getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Autowired
    private MemberServiceClient memberServiceClient;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Override
    public void setDao(MemberDao memberDao) {
        this.baseDao = memberDao;
    }

    /**
     * 根据用户凭证（手机号）获取用户信息，若用户不存在，则执行注册
     * @param credential 手机号
     * @param parameters 封装的请求参数，包含验证码等
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    public UserDetails getMemberByCredential(String credential, Map<String, Object> parameters) throws UsernameNotFoundException {
        Map<String, String> params = new HashMap<String, String>(6);
        String code = (String) parameters.get("code");

        params.put("credential", credential);
        params.put("status", MemberStatus.NORMAL.getValue());

        MemberDO memberDO = this.baseDao.selectMemberByCredential(params);

        /**
         * 若用户不存在，且存在验证码，则用户为新用户，需要注册
         */
        if (memberDO == null && code != null) {
            memberDO = registerMember(credential, code, parameters);
        }

        if (memberDO == null) {
            throw new UsernameNotFoundException("注册失败，请检验手机号是否输入正确");
        }

        return new UserAuthenticationDO(credential, memberDO.getPassword(), memberDO.getMemberSeq(), null);
    }

    /**
     * 调用member-service注册会员
     * @param credential 手机号
     * @param code 验证码
     * @param parameters 封装的请求参数
     * @return MemberDO
     */
    protected MemberDO registerMember(String credential,String code, Map<String, Object> parameters) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        String expectMessageCode = (String) valueOps.get(RedisConstants.PHONE_MESSAGE_CODE_PREFIX + credential);

        if (!code.equals(expectMessageCode)) {
            throw new BadCredentialsException(messages.getMessage(
                                              "TopAuthenticationProvider.register.badMessageCode",
                                              "验证码错误"));
        }

        return memberServiceClient.register(credential, code, (Long) parameters.get("brandshopUserId"), (Long) parameters.get("brandshopId"));
    }
}
