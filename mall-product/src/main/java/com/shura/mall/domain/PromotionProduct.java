package com.shura.mall.domain;

import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductFullReduction;
import com.shura.mall.model.pms.PmsProductLadder;
import com.shura.mall.model.pms.PmsSkuStock;
import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 商品的促销信息封装，包括 sku、打折优惠、满减优惠
 */
@Data
public class PromotionProduct extends PmsProduct {

    private List<PmsSkuStock> skuStockList;

    private List<PmsProductLadder> productLadderList;

    private List<PmsProductFullReduction> productFullReductionList;
}
