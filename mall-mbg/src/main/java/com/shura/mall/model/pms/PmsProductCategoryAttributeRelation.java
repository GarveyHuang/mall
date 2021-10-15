package com.shura.mall.model.pms;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

public class PmsProductCategoryAttributeRelation implements Serializable {
    private Long id;

    @ApiModelProperty(value = "商品分类 id")
    private Long productCategoryId;

    @ApiModelProperty(value = "商品属性 id")
    private Long productAttributeId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductAttributeId() {
        return productAttributeId;
    }

    public void setProductAttributeId(Long productAttributeId) {
        this.productAttributeId = productAttributeId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productCategoryId=").append(productCategoryId);
        sb.append(", productAttributeId=").append(productAttributeId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}