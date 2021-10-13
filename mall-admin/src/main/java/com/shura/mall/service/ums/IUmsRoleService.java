package com.shura.mall.service.ums;

import com.shura.mall.model.ums.UmsPermission;
import com.shura.mall.model.ums.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 后台用户角色管理 Service
 */
public interface IUmsRoleService {

    /**
     * 添加角色
     */
    int create(UmsRole role);

    /**
     * 修改角色信息
     */
    int update(Long id, UmsRole role);

    /**
     * 批量删除角色
     */
    int delete(List<Long> ids);

    /**
     * 获取指定角色权限
     */
    List<UmsPermission> getPermissionList(Long roleId);

    /**
     * 修改指定角色的权限
     */
    @Transactional
    int updatePermission(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色列表
     */
    List<UmsRole> list();
}
