package com.shura.mall.domain;

import lombok.Data;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: MQ 取消订单封装对象
 */
@Data
public class MqCancelOrder {

    private Long orderId;

    private Long memberId;
}
