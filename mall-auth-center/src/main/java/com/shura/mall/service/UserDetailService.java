package com.shura.mall.service;

import cn.hutool.core.util.StrUtil;
import com.shura.mall.domain.MemberDetails;
import com.shura.mall.mapper.UmsMemberMapper;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/20
 * @Description: 权限中心加载用户信息的类
 */
@Slf4j
@Component
public class UserDetailService implements UserDetailsService {

    @Resource
    private UmsMemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StrUtil.isBlank(username)) {
            log.warn("用户登录用户名为空：{}", username);
            throw new UsernameNotFoundException("用户名不能为空");
        }

        UmsMember umsMember = getByUserName(username);
        if (null == umsMember) {
            log.warn("根据用户名没有查询到对应的用户：{}", username);
        }

        log.info("根据用户名：{}，获取登录用户信息：{}", username, umsMember);

        return new MemberDetails(umsMember);
    }

    private UmsMember getByUserName(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }

        return null;
    }
}
