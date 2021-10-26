package com.shura.mall.service.ums.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.shura.mall.bo.AdminUserDetails;
import com.shura.mall.dao.ums.UmsAdminPermissionRelationDAO;
import com.shura.mall.dao.ums.UmsAdminRoleRelationDAO;
import com.shura.mall.domain.converter.UmsAdminConverter;
import com.shura.mall.domain.ums.UmsAdminRegisterParam;
import com.shura.mall.mapper.UmsAdminLoginLogMapper;
import com.shura.mall.mapper.UmsAdminMapper;
import com.shura.mall.mapper.UmsAdminPermissionRelationMapper;
import com.shura.mall.mapper.UmsAdminRoleRelationMapper;
import com.shura.mall.model.ums.*;
import com.shura.mall.security.util.JwtTokenUtil;
import com.shura.mall.service.ums.IUmsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: IUmsAdminService 实现类
 */
@Slf4j
@Service("adminService")
public class UmsAdminServiceImpl implements IUmsAdminService {

    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminLoginLogMapper loginLogMapper;
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UmsAdminRoleRelationDAO adminRoleRelationDAO;
    @Autowired
    private UmsAdminPermissionRelationMapper adminPermissionRelationMapper;
    @Autowired
    private UmsAdminPermissionRelationDAO adminPermissionRelationDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public UmsAdmin register(UmsAdminRegisterParam umsAdminRegisterParam) {
        UmsAdmin umsAdmin = UmsAdminConverter.adminRegisterParamToAdminEntity(umsAdminRegisterParam);

        // 查询是否有相同用户名的用户
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(umsAdmin.getUsername());
        List<UmsAdmin> umsAdminList = adminMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(umsAdminList)) {
            return null;
        }

        // 将密码进行加密操作
        String encodePassword = passwordEncoder.encode(umsAdmin.getPassword());
        umsAdmin.setPassword(encodePassword);
        adminMapper.insert(umsAdmin);
        return umsAdmin;
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        // TODO 密码需要客户端加密后传递
        AdminUserDetails userDetails = loadUserByUsername(username);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("密码不正确");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtTokenUtil.generateToken(userDetails);
        addLoginLog(username);

        // 更新登录时间
        UmsAdmin admin = new UmsAdmin();
        admin.setId(userDetails.getAdminId());
        admin.setLoginTime(new Date());
        adminMapper.updateByPrimaryKeySelective(admin);
        return token;
    }

    /**
     * 添加登录日志记录
     * @param username 用户名
     */
    private void addLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        loginLog.setAddress("");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            log.warn("记录用户登录信息异常：{}", username);
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        loginLog.setUserAgent(request.getHeader("User-Agent"));
        loginLogMapper.insertSelective(loginLog);
    }

    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(adminList)) {
            return adminList.get(0);
        }

        return null;
    }

    @Override
    public String refreshToken(String oldToken) {
        return jwtTokenUtil.refreshHeadToken(oldToken);
    }

    @Override
    public UmsAdmin getItem(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public AdminUserDetails loadUserByUsername(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsPermission> permissionList = getPermissionList(admin.getId());
            return new AdminUserDetails(admin, permissionList);
        }

        throw new UsernameNotFoundException("用户名错误");
    }

    @Override
    public List<UmsAdmin> list(String name, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminExample example = new UmsAdminExample();
        UmsAdminExample.Criteria criteria = example.createCriteria();
        if (!StrUtil.isBlank(name)) {
            criteria.andUsernameLike(name + "%");
            example.or(example.createCriteria().andNickNameLike(name + "%"));
        }
        return adminMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsAdmin umsAdmin) {
        umsAdmin.setId(id);
        // 密码已经加密处理，需要单独修改
        umsAdmin.setPassword(null);
        return adminMapper.updateByPrimaryKeySelective(umsAdmin);
    }

    @Override
    public int delete(Long id) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setId(id);
        umsAdmin.setStatus(-1);
        return adminMapper.updateByPrimaryKeySelective(umsAdmin);
    }

    @Override
    public int updateRole(Long adminId, List<Long> roleIds) {
        int count = roleIds == null ? 0 : roleIds.size();

        // 先删除原来的关系
        UmsAdminRoleRelationExample adminRoleRelationExample = new UmsAdminRoleRelationExample();
        adminRoleRelationExample.createCriteria().andAdminIdEqualTo(adminId);
        adminRoleRelationMapper.deleteByExample(adminRoleRelationExample);

        // 建立新关系
        if (count > 0) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation roleRelation = new UmsAdminRoleRelation();
                roleRelation.setAdminId(adminId);
                roleRelation.setRoleId(roleId);
                list.add(roleRelation);
            }
            adminRoleRelationDAO.insertList(list);
        }
        return count;
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return adminRoleRelationDAO.getRoleList(adminId);
    }

    @Override
    public int updatePermission(Long adminId, List<Long> permissionIds) {
        // 删除原所有权限关系
        UmsAdminPermissionRelationExample relationExample = new UmsAdminPermissionRelationExample();
        relationExample.createCriteria().andAdminIdEqualTo(adminId);
        adminPermissionRelationMapper.deleteByExample(relationExample);

        // 获取用户所有角色权限
        List<UmsPermission> permissionList = adminRoleRelationDAO.getRolePermissionList(adminId);
        List<Long> rolePermissionList = permissionList.stream().map(UmsPermission::getId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<UmsAdminPermissionRelation> relationList = new ArrayList<>();
            // 筛选出 + 权限
            List<Long> addPermissionIdList = permissionIds.stream().filter(permissionId -> ! rolePermissionList.contains(permissionId)).collect(Collectors.toList());
            // 筛选出 - 权限
            List<Long> subPermissionIdList = permissionIds.stream().filter(permissionId -> !permissionIds.contains(permissionId)).collect(Collectors.toList());
            // 插入 +- 权限关系
            relationList.addAll(convert(adminId, 1, addPermissionIdList));
            relationList.addAll(convert(adminId, -1, subPermissionIdList));
            return adminPermissionRelationDAO.insertList(relationList);
        }
        return 0;
    }

    /**
     * 将 +- 权限关系转化为对象
     * @param adminId 用户 ID
     * @param type 权限类型
     * @param permissionIdList 权限 ID 列表
     * @return
     */
    private List<UmsAdminPermissionRelation> convert(Long adminId, int type, List<Long> permissionIdList) {
        return permissionIdList.stream().map(permissionId -> {
            UmsAdminPermissionRelation relation = new UmsAdminPermissionRelation();
            relation.setAdminId(adminId);
            relation.setPermissionId(permissionId);
            return relation;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UmsPermission> getPermissionList(Long adminId) {
        return adminRoleRelationDAO.getPermissionList(adminId);
    }
}
