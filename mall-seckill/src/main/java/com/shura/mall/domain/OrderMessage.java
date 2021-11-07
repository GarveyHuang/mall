package com.shura.mall.domain;

import com.shura.mall.model.oms.OmsOrder;
import com.shura.mall.model.oms.OmsOrderItem;
import lombok.Data;

import java.util.Date;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description:
 */
@Data
public class OrderMessage {

    private OmsOrder order;

    private OmsOrderItem orderItem;

    /**
     * 秒杀活动记录 id
     */
    private Long flashPromotionRelationId;

    /**
     * 限购数量
     */
    private Integer flashPromotionLimit;

    /**
     * 秒杀活动结束日期
     */
    private Date flashPromotionEndDate;
}
