package com.topbaby.cloud.authservice.service;

import com.topbaby.cloud.authservice.entity.MemberCredentialsDO;
import com.topbaby.cloud.authservice.mappers.MemberCredentialsDao;
import com.topbaby.data.AbstractDataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>会员登录信息 Service</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberCredentialsService extends AbstractDataAccessor<MemberCredentialsDO, MemberCredentialsDao> {

    @Autowired
    @Override
    public void setDao(MemberCredentialsDao memberCredentialsDao) {
        this.baseDao = memberCredentialsDao;
    }
}
