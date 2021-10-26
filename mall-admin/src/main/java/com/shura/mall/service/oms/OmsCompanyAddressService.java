package com.shura.mall.service.oms;

import com.shura.mall.model.oms.OmsCompanyAddress;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 收货地址管 Service
 */
public interface OmsCompanyAddressService {

    /**
     * 获取全部收货地址
     */
    List<OmsCompanyAddress> list();
}
