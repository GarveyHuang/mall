package com.shura.mall.domain;

import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 创建订单的参数
 */
@Data
public class OrderParam {

    // 收货地址 id
    private Long memberReceiveAddressId;

    // 优惠券 id
    private Long couponId;

    // 使用的积分数
    private Integer useIntegration;

    // 支付方式
    private Integer payType;

    // 选择购买的购物车商品
    private List<Long> itemIds;
}
