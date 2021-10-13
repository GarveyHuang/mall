package com.shura.mall.controller.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.ums.UmsMemberLevel;
import com.shura.mall.service.ums.IUmsMemberLevelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 会员等级管理
 */
@Api(tags = "UmsMemberLevelController", value = "会员等级管理")
@RestController
@RequestMapping("/memberLevel")
public class UmsMemberLevelController {

    @Autowired
    private IUmsMemberLevelService memberLevelService;

    @ApiOperation(value = "查询所有会员等级")
    @GetMapping(value = "/list")
    public CommonResult<List<UmsMemberLevel>> list(@RequestParam("defaultStatus") Integer defaultStatus) {
        return CommonResult.success(memberLevelService.list(defaultStatus));
    }
}
