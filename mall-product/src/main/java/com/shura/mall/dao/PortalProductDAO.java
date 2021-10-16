package com.shura.mall.dao;

import com.shura.mall.domain.CartProduct;
import com.shura.mall.domain.PmsProductParam;
import com.shura.mall.domain.PromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 商品详情信息自定义 DAO
 */
public interface PortalProductDAO {

    CartProduct getCartProduct(@Param("id") Long id);

    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    PmsProductParam getProductInfo(@Param("id") Long id);

    List<Long> getAllProductId();
}
