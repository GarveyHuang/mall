package com.shura.mall.service.oms;

import com.shura.mall.model.oms.OmsOrderSetting;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单设置 Service
 */
public interface OmsOrderSettingService {

    /**
     * 获取指定订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
