package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsProductAttributeValue;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品参数、商品规格属性自定义 DAO
 */
public interface PmsProductAttributeValueDAO {

    int insertList(@Param("list") List<PmsProductAttributeValue> productAttributeValueList);
}
