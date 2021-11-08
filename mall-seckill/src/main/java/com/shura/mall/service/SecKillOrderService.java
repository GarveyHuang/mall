package com.shura.mall.service;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.domain.OrderParam;
import com.shura.mall.domain.PmsProductParam;
import com.shura.mall.model.oms.OmsOrder;
import com.shura.mall.model.oms.OmsOrderItem;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 秒杀订单管理 Service
 */
public interface SecKillOrderService {

    /**
     * 秒杀订单确认
     */
    CommonResult generateConfirmSecKillOrder(Long productId, Long memberId, String token) throws BusinessException;

    /**
     * 秒杀订单下单
     */
    CommonResult generateSecKillOrder(OrderParam orderParam, Long memberId, String token) throws BusinessException;

    /**
     * 还原 Redis 库存，每次加 1
     */
    void incrRedisStock(Long productId);

    /**
     * 判断是否应该 pub 消息清除集群服务本地的售罄标识
     */
    boolean shouldPublishCleanMsg(Long productId);

    /**
     * 异步下单
     */
    Long asyncCreateOrder(OmsOrder order, OmsOrderItem orderItem, Long flashPromotionRelation);

    /**
     * 获取产品信息
     */
    PmsProductParam getProductInfo(Long productId);
}
