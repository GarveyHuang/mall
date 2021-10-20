package com.shura.mall.controller.ums;

import cn.hutool.core.util.StrUtil;
import com.shura.mall.bo.LoginInfo;
import com.shura.mall.bo.LoginUserDetails;
import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dto.ums.UmsAdminLoginParam;
import com.shura.mall.dto.ums.UmsAdminRegisterParam;
import com.shura.mall.model.ums.UmsAdmin;
import com.shura.mall.model.ums.UmsPermission;
import com.shura.mall.model.ums.UmsRole;
import com.shura.mall.service.ums.IUmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/10
 * @description: 后台用户管理
 */
@RestController
@Api(tags = "UmsAdminController", value = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private IUmsAdminService adminService;

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "/register")
    public CommonResult<UmsAdmin> register(@RequestBody @Validated UmsAdminRegisterParam umsAdminRegisterParam) {
        UmsAdmin admin = adminService.register(umsAdminRegisterParam);
        return CommonResult.success(admin);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public CommonResult<LoginInfo> login(@RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (StrUtil.isBlank(token)) {
            return CommonResult.validateFailed("用户名或密码错误");
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setToken(token);
        loginInfo.setTokenHead(tokenHead);
        return CommonResult.success(loginInfo);
    }

    @ApiOperation(value = "刷新 token")
    @GetMapping(value = "/refreshToken")
    public CommonResult<LoginInfo> refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (StrUtil.isBlank(refreshToken)) {
            return CommonResult.failed("token 已过期！");
        }

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setToken(refreshToken);
        loginInfo.setTokenHead(tokenHead);
        return CommonResult.success(loginInfo);
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value = "/info")
    public CommonResult<LoginUserDetails> getAdminInfo(Principal principal) {
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        LoginUserDetails loginUserDetails = new LoginUserDetails();
        loginUserDetails.setUsername(umsAdmin.getUsername());
        loginUserDetails.setIcon(umsAdmin.getIcon());
        loginUserDetails.setRoles(Collections.singletonList("test"));
        return CommonResult.success(loginUserDetails);
    }

    @ApiOperation(value = "登出")
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    @ApiOperation(value = "根据用户名或姓名分页获取用户列表")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<UmsAdmin>> list(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSzie,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<UmsAdmin> adminList = adminService.list(name, pageSzie, pageNum);
        return CommonResult.success(CommonPage.restPage(adminList));
    }

    @ApiOperation(value = "获取指定用户信息")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsAdmin> getItem(@PathVariable Long id) {
        UmsAdmin admin = adminService.getItem(id);
        return CommonResult.success(admin);
    }

    @ApiOperation(value = "修改指定用户信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        int count = adminService.update(id, admin);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "删除指定用户")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = adminService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "给指定用户分配角色")
    @PostMapping(value = "/role/update/{adminId}")
    public CommonResult updateRole(@PathVariable Long adminId, @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "获取指定用户的角色")
    @GetMapping(value = "/role/{adminId}")
    public CommonResult<List<UmsRole>> getRoleList(@PathVariable Long adminId) {
        List<UmsRole> roleList = adminService.getRoleList(adminId);
        return CommonResult.success(roleList);
    }

    @ApiOperation(value = "给指定用户分配 +- 角色")
    @PostMapping(value = "/permission/update/{adminId}")
    public CommonResult updatePermission(@PathVariable Long adminId,
                                         @RequestParam("permissionIds") List<Long> permissionIds) {
        int count = adminService.updatePermission(adminId, permissionIds);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "获取用户所有权限（包括 +- 权限）")
    @GetMapping(value = "/permission/{adminId}")
    public CommonResult<List<UmsPermission>> getPermissionList(@PathVariable Long adminId) {
        List<UmsPermission> permissionList = adminService.getPermissionList(adminId);
        return CommonResult.success(permissionList);
    }
}
