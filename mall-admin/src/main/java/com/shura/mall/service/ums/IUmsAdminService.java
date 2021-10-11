package com.shura.mall.service.ums;

import com.shura.mall.dto.UmsAdminRegisterParam;
import com.shura.mall.model.ums.UmsAdmin;
import com.shura.mall.model.ums.UmsPermission;
import com.shura.mall.model.ums.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 后台管理员 Service
 */
public interface IUmsAdminService {

    /**
     * 用户注册
     * @param umsAdminRegisterParam
     * @return
     */
    UmsAdmin register(UmsAdminRegisterParam umsAdminRegisterParam);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 生成的 JWT 的 token
     */
    String login(String username, String password);

    /**
     * 根据用户名获取后台用户
     * @param username 用户名
     * @return
     */
    UmsAdmin getAdminByUsername(String username);

    /**
     * 刷新 token
     * @param oldToken 旧的 token
     * @return
     */
    String refreshToken(String oldToken);

    /**
     * 根据用户 ID 获取用户
     * @param id
     * @return
     */
    UmsAdmin getItem(Long id);

    /**
     * 获取用户信息
     * @param username 用户名
     * @return
     */
    UserDetails loadUserByUsername(String username);

    /**
     * 根据用户名或昵称分页查询用户信息
     * @param name
     * @param pageSize
     * @param pageNum
     * @return
     */
    List<UmsAdmin> list(String name, Integer pageSize, Integer pageNum);

    /**
     * 修改指定用户信息
     * @param id
     * @param umsAdmin
     * @return
     */
    int update(Long id, UmsAdmin umsAdmin);

    /**
     * 删除指定用户
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 修改用户角色
     * @param adminId
     * @param roleIds
     * @return
     */
    @Transactional
    int updateRole(Long adminId, List<Long> roleIds);

    /**
     * 获取用户对应角色
     * @param adminId
     * @return
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 修改用户的 +- 权限
     * @param adminId
     * @param permissionIds
     * @return
     */
    @Transactional
     int updatePermission(Long adminId, List<Long> permissionIds);

    /**
     * 获取用户所有权限（包括角色权限和 +- 权限）
     * @param adminId 用户ID
     * @return
     */
    List<UmsPermission> getPermissionList(Long adminId);
}
