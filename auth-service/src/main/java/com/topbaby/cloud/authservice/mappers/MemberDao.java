package com.topbaby.cloud.authservice.mappers;

import com.topbaby.cloud.authservice.entity.MemberDO;
import com.topbaby.data.DaoProvider;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>会员Dao</p>
 *
 * @author SunXiaoYuan
 * @date 18-3-23
 * @since 1.0.0
 */
@Mapper
public interface MemberDao extends DaoProvider<MemberDO> {
    /**
     * 根据会员凭证（手机号）查询会员信息
     *
     * @param params 筛选参数
     * @return MemberDO
     */
    MemberDO selectMemberByCredential(Map<String, String> params);

}
