package com.shura.mall.dao.oms;

import com.shura.mall.dto.oms.OmsOrderDeliveryParam;
import com.shura.mall.dto.oms.OmsOrderDetail;
import com.shura.mall.dto.oms.OmsOrderQueryParam;
import com.shura.mall.model.oms.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单管理自定义 DAO
 */
public interface OmsOrderDAO {

    /**
     * 条件查询订单
     */
    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}
