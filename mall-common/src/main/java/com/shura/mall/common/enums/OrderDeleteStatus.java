package com.shura.mall.common.enums;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 订单删除状态枚举
 */
public enum OrderDeleteStatus {

    NORMAL(0, "未删除"),
    DELETED(1, "已删除");

    private final int status;
    private final String remark;

    OrderDeleteStatus(int status, String remark) {
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
