package com.shura.mall.dao;

import com.shura.mall.domain.FlashPromotionParam;
import com.shura.mall.domain.FlashPromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 促销商品信息自定义 DAO
 */
public interface FlashPromotionProductDAO {

    List<FlashPromotionProduct> getFlashProductList(@Param("flashPromotionId") Long flashPromotionId,
                                                    @Param("sessionId") Long sessionId);

    List<FlashPromotionProduct> getHomePromotionProductList(@Param("promotionIds") List<Long> promotionIds);

    FlashPromotionParam getFlashPromotion(@Param("promotionId") Long promotionId);
}
