package com.shura.mall.common.enums;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 支付方式枚举
 */
public enum PayType {

    UNPAID(0, "未支付"),
    ALI_PAY(1, "支付宝"),
    WECHAT_PAY(2, "微信");

    private final int type;
    private final String remark;

    PayType(int type, String remark) {
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
