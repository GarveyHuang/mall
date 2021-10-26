package com.shura.mall.dao.pms;

import com.shura.mall.domain.pms.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品分类自定 DAO
 */
public interface PmsProductCategoryDAO {

    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
