package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsProductAttribute;
import com.shura.mall.model.pms.PmsProductAttributeCategory;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 包含有分类下属性的 DTO
 */
public class PmsProductAttributeCategoryItem extends PmsProductAttributeCategory {

    private List<PmsProductAttribute> productAttributeList;

    public List<PmsProductAttribute> getProductAttributeList() {
        return productAttributeList;
    }

    public void setProductAttributeList(List<PmsProductAttribute> productAttributeList) {
        this.productAttributeList = productAttributeList;
    }
}
