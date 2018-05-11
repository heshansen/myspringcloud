package com.topbaby.cloud.authservice.mappers;

import com.topbaby.cloud.authservice.entity.MemberCredentialsDO;
import com.topbaby.data.DaoProvider;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>会员登录信息 Dao</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@Mapper
public interface MemberCredentialsDao extends DaoProvider<MemberCredentialsDO> {
}
