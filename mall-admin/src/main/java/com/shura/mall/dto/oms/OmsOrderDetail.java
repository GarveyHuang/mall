package com.shura.mall.dto.oms;

import com.shura.mall.model.oms.OmsOrder;
import com.shura.mall.model.oms.OmsOrderItem;
import com.shura.mall.model.oms.OmsOrderOperateHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单详情信息
 */
@Getter
@Setter
public class OmsOrderDetail extends OmsOrder {

    private List<OmsOrderItem> orderItemList;

    private List<OmsOrderOperateHistory> historyList;
}
