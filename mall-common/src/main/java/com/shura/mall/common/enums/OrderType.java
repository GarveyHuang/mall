package com.shura.mall.common.enums;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 订单类型枚举
 */
public enum OrderType {

    NORMAL(0, "普通订单"),
    SEC_KILL(1, "秒杀订单");

    private final int type;
    private final String remark;

    OrderType(int type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
