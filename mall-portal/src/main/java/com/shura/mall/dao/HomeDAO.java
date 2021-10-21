package com.shura.mall.dao;

import com.shura.mall.model.cms.CmsSubject;
import com.shura.mall.model.pms.PmsBrand;
import com.shura.mall.model.pms.PmsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 首页内容管理自定义 DAO
 */
public interface HomeDAO {

    /**
     * 获取推荐品牌
     */
    List<PmsBrand> getRecommendBrandList(@Param("offset") Integer offset, @Param("limit") Integer limit);
    /**
     * 获取新品推荐
     */
    List<PmsProduct> getNewProductList(@Param("offset") Integer offset, @Param("limit") Integer limit);
    /**
     * 获取人气推荐
     */
    List<PmsProduct> getHotProductList(@Param("offset") Integer offset,@Param("limit") Integer limit);

    /**
     * 获取推荐专题
     */
    List<CmsSubject> getRecommendSubjectList(@Param("offset") Integer offset, @Param("limit") Integer limit);
}
