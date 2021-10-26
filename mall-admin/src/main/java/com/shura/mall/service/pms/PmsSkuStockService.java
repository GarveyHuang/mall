package com.shura.mall.service.pms;

import com.shura.mall.model.pms.PmsSkuStock;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: sku 商品库存管理 Service
 */
public interface PmsSkuStockService {

    /**
     * 根据产品id和skuCode模糊搜索
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
