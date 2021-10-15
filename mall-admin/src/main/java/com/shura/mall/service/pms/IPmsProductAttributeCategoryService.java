package com.shura.mall.service.pms;

import com.shura.mall.dao.pms.PmsProductAttributeCategoryItem;
import com.shura.mall.model.pms.PmsProductAttributeCategory;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品属性分类 Service
 */
public interface IPmsProductAttributeCategoryService {

    int create(String name);

    int update(Long id, String name);

    int delete(Long id);

    PmsProductAttributeCategory getItem(Long id);

    List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum);

    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
