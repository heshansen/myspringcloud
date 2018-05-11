package com.topbaby.cloud.authservice.service;

import com.topbaby.cloud.authservice.entity.BaseUserDetails;
import com.topbaby.cloud.authservice.entity.UserAuthenticationDO;
import com.topbaby.cloud.authservice.enums.UserAppid;
import com.topbaby.cloud.authservice.mappers.UserDao;
import com.topbaby.data.AbstractDataAccessor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserService extends AbstractDataAccessor<BaseUserDetails, UserDao> implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    @Override
    public void setDao(UserDao userDao) {
        this.baseDao = userDao;
    }

    @Override
    public BaseUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Map<String, String> params = new HashMap<String, String>(2);
        params.put("loginid", username);
        params.put("appid", UserAppid.BSS.getValue());
        BaseUserDetails user = this.baseDao.getUserByName(params);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    /**
     * 根据loginId获取用户信息（）
     *
     * @param loginId 登录凭证
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    public UserDetails getUserByLoginId(String loginId) throws UsernameNotFoundException {
        BaseUserDetails user = this.loadUserByUsername(loginId);
        return new UserAuthenticationDO(loginId, user.getPassword(), user.getUserSeq(), null);
    }
}
