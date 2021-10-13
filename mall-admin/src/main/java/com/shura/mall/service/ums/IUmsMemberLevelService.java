package com.shura.mall.service.ums;

import com.shura.mall.model.ums.UmsMemberLevel;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 会员等级管理 Service
 */
public interface IUmsMemberLevelService {

    /**
     * 获取所有会员登录
     * @param defaultStatus 是否为默认会员
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
