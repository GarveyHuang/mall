package com.shura.mall.dao.pms;

import com.shura.mall.domain.pms.ProductAttrInfo;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品属性自定义 DAO
 */
public interface PmsProductAttributeDAO {

    List<ProductAttrInfo> getProductAttrInfo(@Param("id") Long productCategoryId);
}
