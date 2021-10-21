package com.shura.mall.service;

import com.shura.mall.domain.HomeContentResult;
import com.shura.mall.model.cms.CmsSubject;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductCategory;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 首页内容管理 Service
 */
public interface IHomeService {

    /**
     * 获取首页内容
     */
    HomeContentResult content();

    /**
     * 首页商品推荐
     */
    List<PmsProduct> recommendProductList(Integer pageSize, Integer pageNum);

    /**
     * 获取商品分类
     * @param parentId 0:获取一级分类；其他：获取指定二级分类
     */
    List<PmsProductCategory> getProductCateList(Long parentId);

    /**
     * 根据专题分类分页获取专题
     * @param cateId 专题分类id
     */
    List<CmsSubject> getSubjectList(Long cateId, Integer pageSize, Integer pageNum);
}
