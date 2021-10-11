package com.shura.mall.dao;

import com.shura.mall.model.ums.UmsAdminRoleRelation;
import com.shura.mall.model.ums.UmsPermission;
import com.shura.mall.model.ums.UmsRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 后台用户与角色管理自定义 DAO
 */
public interface UmsAdminRoleRelationDAO {

    /**
     * 批量插入用户角色关系
     */
    int insertList(@Param("list") List<UmsAdminRoleRelation> adminRoleRelationList);

    /**
     * 获取用户所有角色
     */
    List<UmsRole> getRoleList(@Param("adminId") Long adminId);


    /**
     * 获取用户所有角色权限
     */
    List<UmsPermission> getRolePermissionList(@Param("adminId") Long adminId);

    /**
     * 获取用户所有权限（包括 +- 权限）
     */
    List<UmsPermission> getPermissionList(@Param("adminId") Long adminId);
}
