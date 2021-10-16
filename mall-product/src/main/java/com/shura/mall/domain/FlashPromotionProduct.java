package com.shura.mall.domain;

import com.shura.mall.model.pms.PmsProduct;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 秒杀信息和商品对象的封装
 */
@Data
public class FlashPromotionProduct extends PmsProduct {

    private BigDecimal flashPromotionPrice;

    private Integer flashPromotionCount;

    private Integer flashPromotionLimit;
}
