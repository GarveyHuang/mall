package com.shura.mall.service.sms;

import com.shura.mall.model.sms.SmsCouponHistory;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券领取记录管理 Service
 */
public interface ISmsCouponHistoryService {

    /**
     * 分页查询优惠券领取记录
     * @param couponId 优惠券id
     * @param useStatus 使用状态
     * @param orderSn 使用订单号码
     */
    List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
