package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsSkuStock;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品 sku 库存自定义 DAO
 */
public interface PmsSkuStockDAO {

    int insertList(@Param("list") List<PmsSkuStock> skuStockList);

    int replaceList(@Param("list") List<PmsSkuStock> skuStockList);
}
