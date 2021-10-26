package com.shura.mall.domain.oms;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description:
 */
@Getter
@Setter
public class OmsReceiverInfoParam {

    private Long orderId;

    private String receiverName;

    private String receiverPhone;

    private String receiverPostCode;

    private String receiverDetailAddress;

    private String receiverProvince;

    private String receiverCity;

    private String receiverRegion;

    private Integer status;
}
