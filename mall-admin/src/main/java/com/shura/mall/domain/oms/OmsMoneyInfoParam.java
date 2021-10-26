package com.shura.mall.domain.oms;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 修改订单费用信息参数
 */
@Getter
@Setter
public class OmsMoneyInfoParam {

    private Long orderId;

    private BigDecimal freightAmount;

    private BigDecimal discountAmount;

    private Integer status;
}
