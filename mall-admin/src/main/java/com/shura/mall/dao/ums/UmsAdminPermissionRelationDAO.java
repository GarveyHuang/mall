package com.shura.mall.dao.ums;

import com.shura.mall.model.ums.UmsAdminPermissionRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 用户权限自定义 DAO
 */
public interface UmsAdminPermissionRelationDAO {

    int insertList(@Param("list") List<UmsAdminPermissionRelation> list);
}
