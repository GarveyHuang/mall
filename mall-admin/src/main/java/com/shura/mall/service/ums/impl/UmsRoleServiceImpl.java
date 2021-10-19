package com.shura.mall.service.ums.impl;

import com.shura.mall.dao.ums.UmsRolePermissionRelationDAO;
import com.shura.mall.mapper.UmsRoleMapper;
import com.shura.mall.mapper.UmsRolePermissionRelationMapper;
import com.shura.mall.model.ums.*;
import com.shura.mall.service.ums.IUmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 后台用户角色管理 Service 实现类
 */
@Service("roleService")
public class UmsRoleServiceImpl implements IUmsRoleService {

    @Autowired
    private UmsRoleMapper roleMapper;
    @Autowired
    private UmsRolePermissionRelationMapper rolePermissionRelationMapper;
    @Autowired
    private UmsRolePermissionRelationDAO rolePermissionRelationDAO;

    @Override
    public int create(UmsRole role) {
        role.setStatus(1);
        role.setCreateTime(new Date());
        role.setSort(0);
        role.setAdminCount(0);
        return roleMapper.insertSelective(role);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsRoleExample example = new UmsRoleExample();
        example.createCriteria().andIdIn(ids);
        return roleMapper.deleteByExample(example);
    }

    @Override
    public List<UmsPermission> getPermissionList(Long roleId) {
        return rolePermissionRelationDAO.getPermissionList(roleId);
    }

    @Override
    public int updatePermission(Long roleId, List<Long> permissionIds) {
        // 先删除原有关系
        UmsRolePermissionRelationExample example = new UmsRolePermissionRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        rolePermissionRelationMapper.deleteByExample(example);

        // 批量插入新关系
        List<UmsRolePermissionRelation> relationList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            UmsRolePermissionRelation relation = new UmsRolePermissionRelation();
            relation.setRoleId(roleId);
            relation.setPermissionId(permissionId);
            relationList.add(relation);
        }
        return rolePermissionRelationDAO.insertList(relationList);
    }

    @Override
    public List<UmsRole> list() {
        return roleMapper.selectByExample(new UmsRoleExample());
    }
}
