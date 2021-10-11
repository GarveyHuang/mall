package com.shura.mall.controller.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.ums.UmsRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 后台用户角色管理
 */
@Api(tags = "UmsRoleController", value = "后台用户角色管理")
@RestController
@RequestMapping("/role")
public class UmsRoleController {

    @ApiOperation(value = "添加角色")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody UmsRole role) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsRole role) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "批量删除角色")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取相应角色权限")
    @GetMapping(value = "/permission/{roleId}")
    public CommonResult getPermissionList(@PathVariable Long roleId) {
        return CommonResult.success(null);
    }

    @ApiOperation(value = "修改角色权限")
    @PostMapping(value = "/permission/update")
    public CommonResult updatePermission(@RequestParam("roleId") Long roleId,
                                         @RequestParam("permissionIds") List<Long> permissionIds) {
        return CommonResult.failed();
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping(value = "/list")
    public CommonResult list() {
        return CommonResult.success(null);
    }
}
