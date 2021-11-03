package com.shura.mall.service;

import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.model.ums.OmsCartItem;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/11/1
 * @Description: 促销管理 Service
 */
public interface OmsPromotionService {

    /**
     * 计算购物车中的促销活动信息
     */
    List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList);
}
