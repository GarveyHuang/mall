package com.shura.mall.domain;

import com.shura.mall.model.pms.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 创建和修改商品时使用的参数
 */
@Data
public class PmsProductParam extends PmsProduct {

    @ApiModelProperty("商品阶梯价格设置")
    private List<PmsProductLadder> productLadderList;

    @ApiModelProperty("商品满减价格设置")
    private List<PmsProductFullReduction> productFullReductionList;

    @ApiModelProperty("商品会员价格设置")
    private List<PmsMemberPrice> memberPriceList;

    @ApiModelProperty("商品的sku库存信息")
    private List<PmsSkuStock> skuStockList;

    @ApiModelProperty("商品参数及自定义规格属性")
    private List<PmsProductAttributeValue> productAttributeValueList;

    // -------------- 秒杀活动信息 ---------------

    /**
     * 秒杀活动价格
     */
    private BigDecimal flashPromotionPrice;

    /**
     * 活动商品库存数
     */
    private Integer flashPromotionCount;

    /**
     * 活动限购数量
     */
    private Integer flashPromotionLimit;

    /**
     * 秒杀活动状态
     * 0->关闭，1->开启
     */
    private Integer flashPromotionStatus;

    /**
     * 秒杀活动开始日期
     */
    private Date flashPromotionStartDate;

    /**
     * 秒杀活动结束日期
     */
    private Date flashPromotionEndDate;

    /**
     * 秒杀活动记录-对应记录存有库存，限购数量，秒杀价格信息
     */
    private Long flashPromotionRelationId;
}
