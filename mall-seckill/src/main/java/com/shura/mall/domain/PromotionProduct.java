package com.shura.mall.domain;

import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductFullReduction;
import com.shura.mall.model.pms.PmsProductLadder;
import com.shura.mall.model.pms.PmsSkuStock;
import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/11/1
 * @Description: 商品的促销信息，包括 sku、打折优惠、满减优惠
 */
@Data
public class PromotionProduct extends PmsProduct {

    /**
     * 商品库存信息
     */
    private List<PmsSkuStock> skuStockList;

    /**
     * 商品打折信息
     */
    private List<PmsProductLadder> productLadderList;

    /**
     * 商品满减信息
     */
    private List<PmsProductFullReduction> productFullReductionList;
}
