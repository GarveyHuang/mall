package com.shura.mall.domain;

import com.shura.mall.model.sms.SmsFlashPromotion;
import com.shura.mall.model.sms.SmsFlashPromotionProductRelation;
import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description:
 */
@Data
public class FlashPromotionParam extends SmsFlashPromotion {

    /**
     * 一个产品只能与该秒杀活动的一个活动场次关联，比如参加了 10 点场，就不能再参加 12 点场
     */
    private List<SmsFlashPromotionProductRelation> relationList;
}
