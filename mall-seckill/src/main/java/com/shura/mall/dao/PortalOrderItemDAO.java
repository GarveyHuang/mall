package com.shura.mall.dao;

import com.shura.mall.model.oms.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 订单商品信息自定义 DAO
 */
public interface PortalOrderItemDAO {

    int insertList(@Param("list") List<OmsOrderItem> list);
}
