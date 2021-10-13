package com.shura.mall.service.ums.impl;

import com.shura.mall.mapper.UmsMemberLevelMapper;
import com.shura.mall.model.ums.UmsMemberLevel;
import com.shura.mall.model.ums.UmsMemberLevelExample;
import com.shura.mall.service.ums.IUmsMemberLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 会员等级管理 Service 实现类
 */
@Service("memberLevelService")
public class UmsMemberLevelServiceImpl implements IUmsMemberLevelService {

    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        UmsMemberLevelExample example = new UmsMemberLevelExample();
        example.createCriteria().andDefaultStatusEqualTo(defaultStatus);
        return memberLevelMapper.selectByExample(example);
    }
}
