package com.shura.mall.service.impl;

import com.shura.mall.dao.PortalMemberInfoDAO;
import com.shura.mall.domain.PortalMemberInfo;
import com.shura.mall.service.UmsMemberCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 会员中心 Service 实现类
 */
@Service("memberCenterService")
public class UmsMemberCenterServiceImpl implements UmsMemberCenterService {

    @Autowired
    private PortalMemberInfoDAO portalMemberInfoDAO;

    @Override
    public PortalMemberInfo getMemberInfo(Long memberId) {
        return portalMemberInfoDAO.getMemberInfo(memberId);
    }
}
