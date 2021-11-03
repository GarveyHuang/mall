package com.shura.mall.domain;

import com.shura.mall.model.sms.SmsCoupon;
import com.shura.mall.model.sms.SmsCouponHistory;
import com.shura.mall.model.sms.SmsCouponProductCategoryRelation;
import com.shura.mall.model.sms.SmsCouponProductRelation;
import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 优惠券领取历史详情封装
 */
@Data
public class SmsCouponHistoryDetail extends SmsCouponHistory {

    /**
     * 相关优惠券信息
     */
    private SmsCoupon coupon;

    /**
     * 优惠券关联商品
     */
    private List<SmsCouponProductRelation> productRelationList;

    /**
     * 优惠券关联商品分类
     */
    private List<SmsCouponProductCategoryRelation> categoryRelationList;
}
