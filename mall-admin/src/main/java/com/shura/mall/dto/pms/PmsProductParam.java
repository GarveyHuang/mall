package com.shura.mall.dto.pms;

import com.shura.mall.model.cms.CmsPreferenceAreaProductRelation;
import com.shura.mall.model.cms.CmsSubjectProductRelation;
import com.shura.mall.model.pms.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 创建和修改商品时使用的参数
 */
@Getter
@Setter
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

    @ApiModelProperty("专题和商品关系")
    private List<CmsSubjectProductRelation> subjectProductRelationList;

    @ApiModelProperty("优选专区和商品的关系")
    private List<CmsPreferenceAreaProductRelation> preferenceAreaProductRelationList;
}
