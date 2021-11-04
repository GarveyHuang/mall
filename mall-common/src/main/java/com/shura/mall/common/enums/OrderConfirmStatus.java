package com.shura.mall.common.enums;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 订单确认状态枚举
 */
public enum OrderConfirmStatus {

    UNCONFIRMED(0, "未确认"),
    CONFIRMED(1, "已确认");

    private final int status;
    private final String remark;

    OrderConfirmStatus(int status, String remark) {
        this.status = status;
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }
}
