package com.shura.mall.controller.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.ums.UmsRole;
import com.shura.mall.service.ums.IUmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IUmsRoleService roleService;

    @ApiOperation(value = "添加角色")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody UmsRole role) {
        int count = roleService.create(role);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "修改角色")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsRole role) {
        int count = roleService.update(id, role);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "批量删除角色")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = roleService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "获取相应角色权限")
    @GetMapping(value = "/permission/{roleId}")
    public CommonResult getPermissionList(@PathVariable Long roleId) {
        return CommonResult.success(roleService.getPermissionList(roleId));
    }

    @ApiOperation(value = "修改角色权限")
    @PostMapping(value = "/permission/update")
    public CommonResult updatePermission(@RequestParam("roleId") Long roleId,
                                         @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = roleService.updatePermission(roleId, permissionIds);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping(value = "/list")
    public CommonResult list() {
        return CommonResult.success(roleService.list());
    }
}
