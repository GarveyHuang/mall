package com.shura.mall.service;

import com.shura.mall.domain.PortalMemberInfo;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 会员中心 Service
 */
public interface UmsMemberCenterService {

    /**
     * 查询会员信息
     * @param memberId
     * @return
     */
    PortalMemberInfo getMemberInfo(Long memberId);
}
