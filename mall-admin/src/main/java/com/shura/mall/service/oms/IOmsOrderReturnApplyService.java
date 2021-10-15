package com.shura.mall.service.oms;

import com.shura.mall.dto.oms.OmsOrderReturnApplyResult;
import com.shura.mall.dto.oms.OmsReturnApplyQueryParam;
import com.shura.mall.dto.oms.OmsUpdateStatusParam;
import com.shura.mall.model.oms.OmsOrderReturnApply;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 退货申请管理 Service
 */
public interface IOmsOrderReturnApplyService {

    /**
     * 分页查询申请
     */
    List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量删除申请
     */
    int delete(List<Long> ids);

    /**
     * 修改申请状态
     */
    int updateStatus(Long id, OmsUpdateStatusParam statusParam);

    /**
     * 获取指定申请详情
     */
    OmsOrderReturnApplyResult getItem(Long id);
}
