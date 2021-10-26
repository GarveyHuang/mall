package com.shura.mall.service.oms.impl;

import com.shura.mall.mapper.OmsOrderSettingMapper;
import com.shura.mall.model.oms.OmsOrderSetting;
import com.shura.mall.service.oms.OmsOrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单设置 Service 实现类
 */
@Service("OrderSettingService")
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {

    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;

    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        return orderSettingMapper.updateByPrimaryKey(orderSetting);
    }
}
