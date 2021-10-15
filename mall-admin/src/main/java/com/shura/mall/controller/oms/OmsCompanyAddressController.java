package com.shura.mall.controller.oms;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.oms.OmsCompanyAddress;
import com.shura.mall.service.oms.IOmsCompanyAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 收货地址管理 Controller
 */
@Api(tags = "OmsCompanyAddressController", value = "收货地址管理")
@RestController
@RequestMapping("/companyAddress")
public class OmsCompanyAddressController {

    @Autowired
    private IOmsCompanyAddressService companyAddressService;

    @ApiOperation("获取所有收货地址")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCompanyAddress>> list() {
        List<OmsCompanyAddress> companyAddressList = companyAddressService.list();
        return CommonResult.success(companyAddressList);
    }
}
