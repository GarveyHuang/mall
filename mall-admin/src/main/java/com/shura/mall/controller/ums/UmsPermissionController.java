package com.shura.mall.controller.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.ums.UmsPermission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 后台用户权限管理
 */
@Api(tags = "UmsPermissionController", value = "后台用户权限管理")
@RestController
@RequestMapping("/permission")
public class UmsPermissionController {

    @ApiOperation(value = "添加权限")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody UmsPermission permission) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "修改权限")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsPermission permission) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "批量删除权限")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "以层级结构返回所有权限")
    @GetMapping(value = "/treeList")
    public CommonResult treeList() {
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取所有权限列表")
    @GetMapping(value = "/list")
    public CommonResult list() {
        return CommonResult.failed();
    }
}
