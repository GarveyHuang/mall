package com.shura.mall.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 当前登录用户信息
 */
@Getter
@Setter
public class LoginUserDetails {

    private String username;

    private String icon;

    private List<String> roles;
}
