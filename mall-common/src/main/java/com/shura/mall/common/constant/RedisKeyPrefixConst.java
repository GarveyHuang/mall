package com.shura.mall.common.constant;

/**
 * @author: Garvey
 * @date: 2021/10/10 10:14 下午
 * @description: 缓存 Key 前缀常量的封装
 */
public interface RedisKeyPrefixConst {

    /**
     * 商品详情内容缓存前缀
     */
    String PRODUCT_DETAIL_CACHE = "product:detail:cache:";

    /**
     * 商品库存缓存
     */
    String MIAOSHA_STOCK_CACHE = "miaosha:stock:cache:";
}
