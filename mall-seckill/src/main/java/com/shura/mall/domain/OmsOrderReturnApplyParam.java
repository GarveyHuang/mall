package com.shura.mall.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 申请退货参数
 */
@Data
public class OmsOrderReturnApplyParam {

    /**
     * 订单 id
     */
    private Long orderId;

    /**
     * 退货商品 id
     */
    private Long productId;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 会员用户名
     */
    private String memberUsername;

    /**
     * 退货人姓名
     */
    private String returnName;

    /**
     * 退货人电话
     */
    private String returnPhone;

    /**
     * 商品图片
     */
    private String productPic;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品品牌
     */
    private String productBrand;

    /**
     * 商品属性：颜色：红色；尺码：xl；
     */
    private String productAttr;

    /**
     * 退货数量
     */
    private Integer productCount;

    /**
     * 商品单价
     */
    private BigDecimal productPrice;

    /**
     * 商品实际支付单价
     */
    private BigDecimal productRealPrice;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 描述
     */
    private String description;

    /**
     * 凭证图片，以逗号隔开
     */
    private String proofPics;
}
