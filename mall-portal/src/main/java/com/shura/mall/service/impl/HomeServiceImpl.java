package com.shura.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.HomeDAO;
import com.shura.mall.domain.HomeContentResult;
import com.shura.mall.feignapi.pms.PmsProductFeignApi;
import com.shura.mall.mapper.CmsSubjectMapper;
import com.shura.mall.mapper.PmsProductCategoryMapper;
import com.shura.mall.mapper.PmsProductMapper;
import com.shura.mall.mapper.SmsHomeAdvertiseMapper;
import com.shura.mall.model.cms.CmsSubject;
import com.shura.mall.model.cms.CmsSubjectExample;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductCategory;
import com.shura.mall.model.pms.PmsProductCategoryExample;
import com.shura.mall.model.pms.PmsProductExample;
import com.shura.mall.model.sms.SmsHomeAdvertise;
import com.shura.mall.model.sms.SmsHomeAdvertiseExample;
import com.shura.mall.service.IHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 首页内容管理 Service 实现类
 */
@Service("homeService")
public class HomeServiceImpl implements IHomeService {

    @Autowired
    private SmsHomeAdvertiseMapper advertiseMapper;

    @Autowired
    private PmsProductMapper productMapper;
    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;
    @Autowired
    private CmsSubjectMapper subjectMapper;

    @Autowired
    private HomeDAO homeDAO;

    @Autowired
    private PmsProductFeignApi pmsProductFeignApi;

    @Override
    public HomeContentResult content() {
        HomeContentResult result = new HomeContentResult();

        // 获取首页广告
        result.setAdvertiseList(getHomeAdvertiseList());
        // 获取推荐品牌
        result.setBrandList(homeDAO.getRecommendBrandList(0, 4));
        // 获取秒杀信息、首页显示
        result.setHomeFlashPromotion(pmsProductFeignApi.getHomeSecKillProductList().getData());
        // 获取新品推荐
        result.setNewProductList(homeDAO.getNewProductList(0, 4));
        // 获取人气推荐
        result.setHotProductList(homeDAO.getHotProductList(0, 4));
        // 获取推荐专题
        result.setSubjectList(homeDAO.getRecommendSubjectList(0, 4));

        return result;
    }

    @Override
    public List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum) {
        // TODO 暂时默认推荐所有商品
        PageHelper.startPage(pageNum, pageSize);
        PmsProductExample example = new PmsProductExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andPublishStatusEqualTo(1);
        return productMapper.selectByExample(example);
    }

    @Override
    public List<PmsProductCategory> getProductCateList(Long parentId) {
        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria()
                .andShowStatusEqualTo(1)
                .andParentIdEqualTo(parentId);
        example.setOrderByClause("sort desc");
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        CmsSubjectExample example = new CmsSubjectExample();
        CmsSubjectExample.Criteria criteria = example.createCriteria();
        criteria.andShowStatusEqualTo(1);
        if (cateId != null) {
            criteria.andCategoryIdEqualTo(cateId);
        }
        return subjectMapper.selectByExample(example);
    }

    private List<SmsHomeAdvertise> getHomeAdvertiseList() {
        SmsHomeAdvertiseExample example = new SmsHomeAdvertiseExample();
        example.createCriteria()
                .andTypeEqualTo(1)
                .andStatusEqualTo(1);
        example.setOrderByClause("sort desc");
        return advertiseMapper.selectByExample(example);
    }
}
