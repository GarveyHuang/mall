package com.shura.mall.dto.sms;

import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.sms.SmsFlashPromotionProductRelation;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description:
 */
public class SmsFlashPromotionProductResult extends SmsFlashPromotionProductRelation {

    private PmsProduct product;

    public PmsProduct getProduct() {
        return product;
    }

    public void setProduct(PmsProduct product) {
        this.product = product;
    }
}
