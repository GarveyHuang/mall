package com.shura.mall.dao.pms;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品属性分类自定义 DAO
 */
public interface PmsProductAttributeCategoryDAO {

    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
