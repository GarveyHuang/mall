package com.shura.mall.dto.pms;

import com.shura.mall.model.pms.PmsProductCategory;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description:
 */
public class PmsProductCategoryWithChildrenItem extends PmsProductCategory {

    private List<PmsProductCategory> children;

    public List<PmsProductCategory> getChildren() {
        return children;
    }

    public void setChildren(List<PmsProductCategory> children) {
        this.children = children;
    }
}
