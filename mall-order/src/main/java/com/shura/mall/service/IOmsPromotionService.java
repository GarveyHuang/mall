package com.shura.mall.service;

import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.model.ums.OmsCartItem;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 促销管理 Service
 */
public interface IOmsPromotionService {

    /**
     * 计算购物车中的促销活动信息
     * @param cartItemList 购物车
     * @return
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
