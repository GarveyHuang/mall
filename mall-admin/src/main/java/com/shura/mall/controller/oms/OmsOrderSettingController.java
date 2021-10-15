package com.shura.mall.controller.oms;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.oms.OmsOrderSetting;
import com.shura.mall.service.oms.IOmsOrderSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单设置管理 Controller
 */
@Api(tags = "OmsOrderSettingController", value = "订单设置管理")
@RestController
@RequestMapping("/orderSetting")
public class OmsOrderSettingController {

    @Autowired
    private IOmsOrderSettingService orderSettingService;

    @ApiOperation("获取指定订单设置")
    @GetMapping(value = "/{id}")
    public CommonResult<OmsOrderSetting> getItem(@PathVariable Long id) {
        OmsOrderSetting orderSetting = orderSettingService.getItem(id);
        return CommonResult.success(orderSetting);
    }

    @ApiOperation("修改指定订单设置")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody OmsOrderSetting orderSetting) {
        int count = orderSettingService.update(id,orderSetting);
        if(count > 0){
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }
}
