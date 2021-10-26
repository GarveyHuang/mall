package com.shura.mall.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.mapper.SmsHomeRecommendSubjectMapper;
import com.shura.mall.model.sms.SmsHomeRecommendSubject;
import com.shura.mall.model.sms.SmsHomeRecommendSubjectExample;
import com.shura.mall.service.sms.SmsHomeRecommendSubjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 首页专题推荐管理 Service 实现类
 */
@Service("homeRecommendSubjectService")
public class SmsHomeRecommendSubjectServiceImpl implements SmsHomeRecommendSubjectService {
    
    @Autowired
    private SmsHomeRecommendSubjectMapper homeRecommendSubjectMapper;

    @Override
    public int create(List<SmsHomeRecommendSubject> recommendSubjectList) {
        for (SmsHomeRecommendSubject recommendProduct : recommendSubjectList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
            homeRecommendSubjectMapper.insert(recommendProduct);
        }
        return recommendSubjectList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendSubject recommendProduct = new SmsHomeRecommendSubject();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return homeRecommendSubjectMapper.updateByPrimaryKeySelective(recommendProduct);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsHomeRecommendSubjectExample example = new SmsHomeRecommendSubjectExample();
        example.createCriteria().andIdIn(ids);
        return homeRecommendSubjectMapper.deleteByExample(example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeRecommendSubjectExample example = new SmsHomeRecommendSubjectExample();
        example.createCriteria().andIdIn(ids);
        SmsHomeRecommendSubject record = new SmsHomeRecommendSubject();
        record.setRecommendStatus(recommendStatus);
        return homeRecommendSubjectMapper.updateByExampleSelective(record,example);
    }

    @Override
    public List<SmsHomeRecommendSubject> list(String subjectName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);

        SmsHomeRecommendSubjectExample example = new SmsHomeRecommendSubjectExample();
        SmsHomeRecommendSubjectExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(subjectName)) {
            criteria.andSubjectNameLike(subjectName + "%");
        }

        if (recommendStatus != null) {
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }

        example.setOrderByClause("sort desc");
        return homeRecommendSubjectMapper.selectByExample(example);
    }
}
