package com.shura.mall.domain;

import com.shura.mall.model.oms.OmsOrder;
import com.shura.mall.model.oms.OmsOrderItem;
import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 包含订单商品信息的订单详情
 */
@Data
public class OmsOrderDetail extends OmsOrder {

    private List<OmsOrderItem> orderItemList;
}
