package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import com.shura.mall.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 会员收货地址管理 Controller
 */
@Api(tags = "UmsMemberReceiveAddressController", value = "会员收货地址管理")
@Slf4j
@RestController
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {

    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @PostMapping(value = "/add")
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address, @RequestHeader("memberId") Long memberId) {
        int count = memberReceiveAddressService.add(address, memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("删除收货地址")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id, @RequestHeader("memberId") Long memberId) {
        int count = memberReceiveAddressService.delete(id,memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("修改收货地址")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address,
                               @RequestHeader("memberId") Long memberId) {
        int count = memberReceiveAddressService.update(id, address,memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("显示所有收货地址")
    @GetMapping(value = "/list")
    public CommonResult<List<UmsMemberReceiveAddress>> list(@RequestHeader("memberId") long memberId) {
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressService.list(memberId);
        return CommonResult.success(addressList);
    }

    @ApiOperation("显示收货地址详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable Long id,
                                                         @RequestHeader("memberId") long memberId) {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(id,memberId);
        return CommonResult.success(address);
    }
}
