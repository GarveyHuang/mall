package com.shura.mall.service;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.domain.ConfirmOrderResult;
import com.shura.mall.domain.MqCancelOrder;
import com.shura.mall.domain.OmsOrderDetail;
import com.shura.mall.domain.OrderParam;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 前台订单管理 Service
 */
public interface OmsPortalOrderService {

    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> itemIds, Long memberId) throws BusinessException;

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    CommonResult generateOrder(OrderParam orderParam, Long memberId) throws BusinessException;

    /**
     * 订单详情
     */
    CommonResult getDetailOrder(Long orderId);

    /**
     * 支付成功后的回调
     */
    @Transactional
    Integer paySuccess(Long orderId, Integer payType);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeoutOrder();

    /**
     * 取消单个订单
     */
    @Transactional
    void cancelOrder(Long orderId, Long memberId);

    /**
     * 删除订单[逻辑删除]，只能 status 为：3 ->已完成；4->已关闭；5->无效订单，才可以删除，
     * 否则只能先取消订单然后删除
     */
    @Transactional
    int deleteOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(MqCancelOrder mqCancelOrder);

    /**
     * 查询会员的订单
     */
    CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status);
}
