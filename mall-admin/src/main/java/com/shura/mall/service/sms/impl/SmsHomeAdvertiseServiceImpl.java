package com.shura.mall.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.mapper.SmsHomeAdvertiseMapper;
import com.shura.mall.model.sms.SmsHomeAdvertise;
import com.shura.mall.model.sms.SmsHomeAdvertiseExample;
import com.shura.mall.service.sms.ISmsHomeAdvertiseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 首页广告管理 Service 实现类
 */
@Service("homeAdvertiseService")
public class SmsHomeAdvertiseServiceImpl implements ISmsHomeAdvertiseService {
    
    @Autowired
    private SmsHomeAdvertiseMapper homeAdvertiseMapper;

    @Override
    public int create(SmsHomeAdvertise advertise) {
        advertise.setClickCount(0);
        advertise.setOrderCount(0);
        return homeAdvertiseMapper.insert(advertise);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria().andIdIn(ids);
        return homeAdvertiseMapper.deleteByExample(example);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsHomeAdvertise advertise = new SmsHomeAdvertise();
        advertise.setId(id);
        advertise.setStatus(status);
        return homeAdvertiseMapper.updateByPrimaryKeySelective(advertise);
    }

    @Override
    public SmsHomeAdvertise getItem(Long id) {
        return homeAdvertiseMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, SmsHomeAdvertise advertise) {
        advertise.setId(id);
        return homeAdvertiseMapper.updateByPrimaryKeySelective(advertise);
    }

    @Override
    public List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        SmsHomeAdvertiseExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(name)) {
            criteria.andNameLike(name + "%");
        }

        if (type != null) {
            criteria.andTypeEqualTo(type);
        }

        if (StringUtils.isNotBlank(endTime)) {
            String startStr = endTime + " 00:00:00";
            String endStr = endTime + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date start = null;
            try {
                start = sdf.parse(startStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date end = null;
            try {
                end = sdf.parse(endStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (start != null && end != null) {
                criteria.andEndTimeBetween(start, end);
            }
        }
        example.setOrderByClause("sort desc");
        return homeAdvertiseMapper.selectByExample(example);
    }
}
