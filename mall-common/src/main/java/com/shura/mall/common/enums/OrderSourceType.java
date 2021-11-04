package com.shura.mall.common.enums;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 订单来源枚举
 */
public enum OrderSourceType {

    PC(0, "PC 订单"),
    APP(1, "APP 订单"),
    WECHAT_APPLETS(2, "微信小程序");

    private final int type;
    private final String remark;

    OrderSourceType(int type, String remark) {
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
