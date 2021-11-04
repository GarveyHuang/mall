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
    String PRODUCT_DETAIL_CACHE_PREFIX = "product:detail:cache:";

    /**
     * 秒杀商品库存缓存
     */
    String SEC_KILL_STOCK_CACHE_PREFIX = "secKill:stock:cache:";

    /**
     * 当库存减到 0 时，需要做一次库存同步，存在预减
     */
    String STOCK_REFRESHED_MESSAGE_PREFIX = "stock:refreshed:message:";

    /**
     * 当天订单自增 id 缓存
     */
    String ORDER_ID_CACHE_PREFIX = "portal:orderId:";

    /**
     * Redis 布隆过滤器 key
     */
    String PRODUCT_REDIS_BLOOM_FILTER = "product:redis:bloom:filter";


    String CLEAN_NO_STOCK_CACHE = "cleanNoStockCache";
}
