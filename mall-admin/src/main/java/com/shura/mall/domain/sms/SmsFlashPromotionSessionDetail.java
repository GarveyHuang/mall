package com.shura.mall.domain.sms;

import com.shura.mall.model.sms.SmsFlashPromotionSession;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description:
 */
public class SmsFlashPromotionSessionDetail extends SmsFlashPromotionSession {

    private Long productCount;

    public Long getProductCount() {
        return productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }
}
