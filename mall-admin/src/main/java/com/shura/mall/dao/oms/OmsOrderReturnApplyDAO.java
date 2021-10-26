package com.shura.mall.dao.oms;

import com.shura.mall.domain.oms.OmsOrderReturnApplyResult;
import com.shura.mall.domain.oms.OmsReturnApplyQueryParam;
import com.shura.mall.model.oms.OmsOrderReturnApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单退货申请自定义 DAO
 */
public interface OmsOrderReturnApplyDAO {

    /**
     * 查询申请列表
     */
    List<OmsOrderReturnApply> getList(@Param("queryParam") OmsReturnApplyQueryParam queryParam);

    /**
     * 获取申请详情
     */
    OmsOrderReturnApplyResult getDetail(@Param("id")Long id);
}
