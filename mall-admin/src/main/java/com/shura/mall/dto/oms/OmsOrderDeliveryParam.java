package com.shura.mall.dto.oms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单发货参数
 */
@Getter
@Setter
public class OmsOrderDeliveryParam {

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("物流公司")
    private String deliveryCompany;

    @ApiModelProperty("物流单号")
    private String deliverySn;
}
