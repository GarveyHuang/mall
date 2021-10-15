package com.shura.mall.dto.sms;

import com.shura.mall.model.sms.SmsCoupon;
import com.shura.mall.model.sms.SmsCouponProductCategoryRelation;
import com.shura.mall.model.sms.SmsCouponProductRelation;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券信息封装，包括绑定商品和绑定分类
 */
public class SmsCouponParam extends SmsCoupon {

    //优惠券绑定的商品
    private List<SmsCouponProductRelation> productRelationList;

    //优惠券绑定的商品分类
    private List<SmsCouponProductCategoryRelation> productCategoryRelationList;

    public List<SmsCouponProductRelation> getProductRelationList() {
        return productRelationList;
    }

    public void setProductRelationList(List<SmsCouponProductRelation> productRelationList) {
        this.productRelationList = productRelationList;
    }

    public List<SmsCouponProductCategoryRelation> getProductCategoryRelationList() {
        return productCategoryRelationList;
    }

    public void setProductCategoryRelationList(List<SmsCouponProductCategoryRelation> productCategoryRelationList) {
        this.productCategoryRelationList = productCategoryRelationList;
    }
}
