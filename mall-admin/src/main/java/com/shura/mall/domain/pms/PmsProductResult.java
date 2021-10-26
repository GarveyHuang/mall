package com.shura.mall.domain.pms;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 查询单个产品进行修改时返回的结果
 */
public class PmsProductResult extends PmsProductParam {

    /**
     * 商品所属分类的父 id
     */
    private Long cateParentId;

    public Long getCateParentId() {
        return cateParentId;
    }

    public void setCateParentId(Long cateParentId) {
        this.cateParentId = cateParentId;
    }
}
