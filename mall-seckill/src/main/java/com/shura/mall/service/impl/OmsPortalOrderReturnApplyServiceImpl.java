package com.shura.mall.service.impl;

import com.shura.mall.domain.OmsOrderReturnApplyParam;
import com.shura.mall.mapper.OmsOrderReturnApplyMapper;
import com.shura.mall.model.oms.OmsOrderReturnApply;
import com.shura.mall.service.OmsPortalOrderReturnApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 订单退货管理 Service 实现类
 */
@Service("portalOrderReturnApplyService")
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {

    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;

    @Override
    public int apply(OmsOrderReturnApplyParam returnApplyParam) {
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApplyParam, returnApply);
        returnApply.setCreateTime(new Date());
        returnApply.setStatus(0);

        return returnApplyMapper.insert(returnApply);
    }
}
