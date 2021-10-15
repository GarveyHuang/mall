package com.shura.mall.service.oms.impl;

import com.shura.mall.mapper.OmsCompanyAddressMapper;
import com.shura.mall.model.oms.OmsCompanyAddress;
import com.shura.mall.model.oms.OmsCompanyAddressExample;
import com.shura.mall.service.oms.IOmsCompanyAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 收货地址管 Service 实现类
 */
@Service("companyAddressService")
public class OmsCompanyAddressServiceImpl implements IOmsCompanyAddressService {

    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;

    @Override
    public List<OmsCompanyAddress> list() {
        return companyAddressMapper.selectByExample(new OmsCompanyAddressExample());
    }
}
