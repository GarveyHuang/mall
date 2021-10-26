package com.shura.mall.service;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.domain.SmsCouponHistoryDetail;
import com.shura.mall.model.sms.SmsCouponHistory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 用户优惠券管理 Service
 */
public interface UmsCouponService {
    /**
     * 会员添加优惠券
     */
    @Transactional
    CommonResult add(Long couponId, Long memberId, String nickName);

    /**
     * 获取优惠券列表
     * @param useStatus 优惠券的使用状态
     */
    List<SmsCouponHistory> list(Integer useStatus, Long memberId);

    /**
     * 根据购物车信息获取可用优惠券
     */
    List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type, Long memberId);
}
