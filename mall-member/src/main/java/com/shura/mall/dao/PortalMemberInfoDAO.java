package com.shura.mall.dao;

import com.shura.mall.domain.PortalMemberInfo;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 会员信息查询自定义 DAO
 */
public interface PortalMemberInfoDAO {

    /**
     * 查询会员信息
     * @param memberId
     * @return
     */
    PortalMemberInfo getMemberInfo(Long memberId);
}
