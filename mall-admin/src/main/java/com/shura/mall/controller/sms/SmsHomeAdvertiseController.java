package com.shura.mall.controller.sms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.sms.SmsHomeAdvertise;
import com.shura.mall.service.sms.ISmsHomeAdvertiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 首页轮播广告管理 Controller
 */
@Api(tags = "SmsHomeAdvertiseController", value = "首页轮播广告管理")
@RestController
@RequestMapping("/home/advertise")
public class SmsHomeAdvertiseController {
    
    @Autowired
    private ISmsHomeAdvertiseService homeAdvertiseService;

    @ApiOperation("添加广告")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody SmsHomeAdvertise advertise) {
        int count = homeAdvertiseService.create(advertise);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("删除广告")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = homeAdvertiseService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("修改上下线状态")
    @PostMapping(value = "/update/status/{id}")
    public CommonResult updateStatus(@PathVariable Long id, Integer status) {
        int count = homeAdvertiseService.updateStatus(id, status);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("获取广告详情")
    @GetMapping(value = "/{id}")
    public CommonResult<SmsHomeAdvertise> getItem(@PathVariable Long id) {
        SmsHomeAdvertise advertise = homeAdvertiseService.getItem(id);
        return CommonResult.success(advertise);
    }

    @ApiOperation("修改广告")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsHomeAdvertise advertise) {
        int count = homeAdvertiseService.update(id, advertise);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("分页查询广告")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsHomeAdvertise>> list(@RequestParam(value = "name", required = false) String name,
                                                           @RequestParam(value = "type", required = false) Integer type,
                                                           @RequestParam(value = "endTime", required = false) String endTime,
                                                           @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeAdvertise> advertiseList = homeAdvertiseService.list(name, type, endTime, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(advertiseList));
    }
}
