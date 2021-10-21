package com.shura.mall.dao;

import com.shura.mall.domain.EsProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 搜索系统中的商品管理自定义 DAO
 */
public interface EsProductDAO {

    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
