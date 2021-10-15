package com.shura.mall.dao.oms;

import com.shura.mall.model.oms.OmsOrderOperateHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单操作记录自定义 DAO
 */
public interface OmsOrderOperateHistoryDAO {

    int insertList(@Param("list") List<OmsOrderOperateHistory> orderOperateHistoryList);
}
