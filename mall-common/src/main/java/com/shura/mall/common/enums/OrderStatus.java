package com.shura.mall.common.enums;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 订单状态枚举
 */
public enum OrderStatus {

    UNPAID(0, "待付款"),
    UNDELIVERED(1, "待发货"),
    DELIVERED(2, "已发货"),
    COMPLETED(3, "已完成"),
    CLOSED(4, "已关闭"),
    INVALID(5, "无效订单");

    private final int status;
    private final String remark;

    OrderStatus(int status, String remark) {
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
