package com.shura.mall.domain;

import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductAttribute;
import com.shura.mall.model.pms.PmsSkuStock;
import lombok.Data;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 购物车中选择规格的商品信息
 */
@Data
public class CartProduct extends PmsProduct {

    private List<PmsProductAttribute> productAttributeList;

    private List<PmsSkuStock> skuStockList;
}
