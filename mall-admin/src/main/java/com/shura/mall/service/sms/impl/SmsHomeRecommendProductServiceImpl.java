package com.shura.mall.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.mapper.SmsHomeRecommendProductMapper;
import com.shura.mall.model.sms.SmsHomeRecommendProduct;
import com.shura.mall.model.sms.SmsHomeRecommendProductExample;
import com.shura.mall.service.sms.ISmsHomeRecommendProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 首页人气推荐管理 Service 实现类
 */
@Service("homeRecommendProductService")
public class SmsHomeRecommendProductServiceImpl implements ISmsHomeRecommendProductService {
    
    @Autowired
    private SmsHomeRecommendProductMapper homeRecommendProductMapper;

    @Override
    public int create(List<SmsHomeRecommendProduct> homeRecommendProductList) {
        for (SmsHomeRecommendProduct recommendProduct : homeRecommendProductList) {
            recommendProduct.setRecommendStatus(1);
            recommendProduct.setSort(0);
            homeRecommendProductMapper.insert(recommendProduct);
        }
        return homeRecommendProductList.size();
    }

    @Override
    public int updateSort(Long id, Integer sort) {
        SmsHomeRecommendProduct recommendProduct = new SmsHomeRecommendProduct();
        recommendProduct.setId(id);
        recommendProduct.setSort(sort);
        return homeRecommendProductMapper.updateByPrimaryKeySelective(recommendProduct);
    }

    @Override
    public int delete(List<Long> ids) {
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        example.createCriteria().andIdIn(ids);
        return homeRecommendProductMapper.deleteByExample(example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        example.createCriteria().andIdIn(ids);
        SmsHomeRecommendProduct record = new SmsHomeRecommendProduct();
        record.setRecommendStatus(recommendStatus);
        return homeRecommendProductMapper.updateByExampleSelective(record,example);
    }

    @Override
    public List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        
        SmsHomeRecommendProductExample example = new SmsHomeRecommendProductExample();
        SmsHomeRecommendProductExample.Criteria criteria = example.createCriteria();
        
        if (StringUtils.isNotBlank(productName)) {
            criteria.andProductNameLike(productName + "%");
        }
        
        if (recommendStatus != null) {
            criteria.andRecommendStatusEqualTo(recommendStatus);
        }
        
        example.setOrderByClause("sort desc");
        return homeRecommendProductMapper.selectByExample(example);
    }
}
