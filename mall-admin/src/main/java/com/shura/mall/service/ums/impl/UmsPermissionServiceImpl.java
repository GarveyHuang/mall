package com.shura.mall.service.ums.impl;

import com.shura.mall.dto.ums.UmsPermissionNode;
import com.shura.mall.mapper.UmsPermissionMapper;
import com.shura.mall.model.ums.UmsPermission;
import com.shura.mall.model.ums.UmsPermissionExample;
import com.shura.mall.service.ums.IUmsPermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 后台用户权限管理 Service 实现类
 */
@Service("permissionService")
public class UmsPermissionServiceImpl implements IUmsPermissionService {

    @Autowired
    private UmsPermissionMapper permissionMapper;

    @Override
    public int create(UmsPermission permission) {
        permission.setStatus(1);
        permission.setCreateTime(new Date());
        permission.setSort(0);
        return permissionMapper.insert(permission);
    }

    @Override
    public int update(Long id, UmsPermission permission) {
        permission.setId(id);
        return permissionMapper.updateByPrimaryKey(permission);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsPermissionExample example = new UmsPermissionExample();
        example.createCriteria().andIdIn(ids);
        return permissionMapper.deleteByExample(example);
    }

    @Override
    public List<UmsPermissionNode> treeList() {
        List<UmsPermission> permissionList = permissionMapper.selectByExample(new UmsPermissionExample());
        return permissionList.stream()
                .filter(permission -> permission.getPid().equals(0L))
                .map(permission -> covert(permission, permissionList))
                .collect(Collectors.toList());
    }

    @Override
    public List<UmsPermission> list() {
        return permissionMapper.selectByExample(new UmsPermissionExample());
    }

    /**
     * 将权限转换为带有子级的权限对象
     * 当找不到子级权限的时候 map 操作不会再递归调用 convert
     * @param permission
     * @param permissionList
     * @return
     */
    private UmsPermissionNode covert(UmsPermission permission, List<UmsPermission> permissionList) {
        UmsPermissionNode node = new UmsPermissionNode();
        BeanUtils.copyProperties(permission, node);
        List<UmsPermissionNode> children = permissionList.stream()
                .filter(subPermission -> subPermission.getPid().equals(permission.getPid()))
                .map(subPermission -> covert(subPermission, permissionList))
                .collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }
}
