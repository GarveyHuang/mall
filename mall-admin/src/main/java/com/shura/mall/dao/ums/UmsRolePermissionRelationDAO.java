package com.shura.mall.dao.ums;

import com.shura.mall.model.ums.UmsPermission;
import com.shura.mall.model.ums.UmsRolePermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 后台用户角色管理自定义 DAO
 */
public interface UmsRolePermissionRelationDAO {

    /**
     * 批量插入角色和权限关系
     */
    int insertList(@Param("list") List<UmsRolePermissionRelation> list);

    /**
     * 根据角色获取权限
     */
    List<UmsPermission> getPermissionList(@Param("roleId") Long roleId);
}
