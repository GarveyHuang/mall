package com.shura.mall.domain.oms;

import com.shura.mall.model.oms.OmsCompanyAddress;
import com.shura.mall.model.oms.OmsOrderReturnApply;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 申请消息结果封装
 */
@Getter
@Setter
public class OmsOrderReturnApplyResult extends OmsOrderReturnApply {

    private OmsCompanyAddress companyAddress;
}
