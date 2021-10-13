package com.shura.mall.service.ums;

import com.shura.mall.dto.ums.UmsPermissionNode;
import com.shura.mall.model.ums.UmsPermission;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 后台用户权限管理 Service
 */
public interface IUmsPermissionService {

    /**
     * 添加权限
     */
    int create(UmsPermission permission);

    /**
     * 修改权限
     */
    int update(Long id,UmsPermission permission);

    /**
     * 批量删除权限
     */
    int delete(List<Long> ids);

    /**
     * 以层级结构返回所有权限
     */
    List<UmsPermissionNode> treeList();

    /**
     * 获取所有权限
     */
    List<UmsPermission> list();
}
