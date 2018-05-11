package com.topbaby.cloud.authservice.mappers;

import com.topbaby.cloud.authservice.entity.BaseUserDetails;
import com.topbaby.data.DaoProvider;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * 用户Dao
 * 用户包含导购员、门店管理员、商户管理员、平台管理员
 *
 * @since 1.0.0
 */
@Mapper
public interface UserDao extends DaoProvider<BaseUserDetails> {
    BaseUserDetails getUserByName(Map<String, String> params);
}
