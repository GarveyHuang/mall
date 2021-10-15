package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsProductCategoryAttributeRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品分类和属性关系自定义 DAO
 */
public interface PmsProductCategoryAttributeRelationDAO {

    int insertList(@Param("list") List<PmsProductCategoryAttributeRelation> productCategoryAttributeRelationList);
}
