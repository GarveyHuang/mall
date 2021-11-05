package com.shura.mall.service;

import com.shura.mall.domain.OmsOrderReturnApplyParam;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 订单退货管理 Service
 */
public interface OmsPortalOrderReturnApplyService {

    int apply(OmsOrderReturnApplyParam returnApplyParam);
}
