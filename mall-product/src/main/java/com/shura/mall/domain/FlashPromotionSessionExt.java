package com.shura.mall.domain;

import com.shura.mall.model.sms.SmsFlashPromotionSession;
import lombok.Data;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 秒杀活动场次信息封装
 */
@Data
public class FlashPromotionSessionExt extends SmsFlashPromotionSession {

    /**
     * 活动状态：0->进行中，1->即将开始，2->已结束
     */
    private Integer sessionStatus;

    /**
     * 当前秒杀活动 id
     */
    private Long flashPromotionId;
}
