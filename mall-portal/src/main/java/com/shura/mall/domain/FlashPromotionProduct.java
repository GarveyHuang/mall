package com.shura.mall.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 秒杀信息和商品对象封装
 */
@Getter
@Setter
public class FlashPromotionProduct {

    private BigDecimal flashPromotionPrice;

    private Integer flashPromotionCount;

    private Integer flashPromotionLimit;
}
