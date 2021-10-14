package com.shura.mall.service.cms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.mapper.CmsSubjectMapper;
import com.shura.mall.model.cms.CmsSubject;
import com.shura.mall.model.cms.CmsSubjectExample;
import com.shura.mall.service.cms.ICmsSubjectService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 商品专题 Service 实现类
 */
@Service("subjectService")
public class CmsSubjectServiceImpl implements ICmsSubjectService {

    @Autowired
    private CmsSubjectMapper subjectMapper;

    @Override
    public List<CmsSubject> listAll() {
        return subjectMapper.selectByExample(new CmsSubjectExample());
    }

    @Override
    public List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CmsSubjectExample example = new CmsSubjectExample();
        CmsSubjectExample.Criteria criteria = example.createCriteria();
        if (!StringUtils.isNotBlank(keyword)) {
            criteria.andTitleLike(keyword + "%");
        }
        return subjectMapper.selectByExample(example);
    }
}
