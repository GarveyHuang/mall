package com.shura.mall.service.pms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.pms.PmsProductCategoryAttributeRelationDAO;
import com.shura.mall.dao.pms.PmsProductCategoryDAO;
import com.shura.mall.dto.pms.PmsProductCategoryParam;
import com.shura.mall.dto.pms.PmsProductCategoryWithChildrenItem;
import com.shura.mall.mapper.PmsProductCategoryAttributeRelationMapper;
import com.shura.mall.mapper.PmsProductCategoryMapper;
import com.shura.mall.mapper.PmsProductMapper;
import com.shura.mall.model.pms.*;
import com.shura.mall.service.pms.IPmsProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品分类 Service 实现类
 */
@Service("productCategoryService")
public class PmsProductCategoryServiceImpl implements IPmsProductCategoryService {

    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;

    @Autowired
    private PmsProductMapper productMapper;

    @Autowired
    private PmsProductCategoryAttributeRelationMapper productCategoryAttributeRelationMapper;

    @Autowired
    private PmsProductCategoryAttributeRelationDAO productCategoryAttributeRelationDAO;

    @Autowired
    private PmsProductCategoryDAO productCategoryDAO;

    @Override
    public int create(PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setProductCount(0);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);

        // 没有父分类时，为一级分类
        setCategoryLevel(productCategory);

        int count = productCategoryMapper.insertSelective(productCategory);

        // 创建筛选属性关联
        List<Long> productAttributeIdList = pmsProductCategoryParam.getProductAttributeIdList();
        if (!CollectionUtils.isEmpty(productAttributeIdList)) {
            insertRelationList(productCategory.getId(), productAttributeIdList);
        }
        return count;
    }

    @Override
    public int update(Long id, PmsProductCategoryParam pmsProductCategoryParam) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setId(id);
        BeanUtils.copyProperties(pmsProductCategoryParam, productCategory);

        // 更新商品分类时，要更新商品中的名称
        PmsProduct product = new PmsProduct();
        product.setProductCategoryName(productCategory.getName());
        PmsProductExample productExample = new PmsProductExample();
        productExample.createCriteria().andProductCategoryIdEqualTo(id);
        productMapper.updateByExampleSelective(product, productExample);

        // 同时更新筛选属性的信息
        PmsProductCategoryAttributeRelationExample relationExample = new PmsProductCategoryAttributeRelationExample();
        relationExample.createCriteria().andProductCategoryIdEqualTo(id);
        productCategoryAttributeRelationMapper.deleteByExample(relationExample);
        if (!CollectionUtils.isEmpty(pmsProductCategoryParam.getProductAttributeIdList())) {
            insertRelationList(id, pmsProductCategoryParam.getProductAttributeIdList());
        }

        return productCategoryMapper.updateByPrimaryKeySelective(productCategory);
    }

    @Override
    public List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andParentIdEqualTo(parentId);
        return productCategoryMapper.selectByExample(example);
    }

    @Override
    public int delete(Long id) {
        return productCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PmsProductCategory getItem(Long id) {
        return productCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateNavStatus(List<Long> ids, Integer navStatus) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setNavStatus(navStatus);

        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andIdIn(ids);
        return productCategoryMapper.updateByExampleSelective(productCategory, example);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsProductCategory productCategory = new PmsProductCategory();
        productCategory.setShowStatus(showStatus);

        PmsProductCategoryExample example = new PmsProductCategoryExample();
        example.createCriteria().andIdIn(ids);
        return productCategoryMapper.updateByExampleSelective(productCategory, example);
    }

    @Override
    public List<PmsProductCategoryWithChildrenItem> listWithChildren() {
        return productCategoryDAO.listWithChildren();
    }

    /**
     * 根据分类的 parentId 设置分类的 level
     */
    private void setCategoryLevel(PmsProductCategory productCategory) {
        // 没有父分类时为一级分类
        if (productCategory.getParentId() == 0) {
            productCategory.setLevel(0);
        } else {
            // 有父分类时选择根据父分类 level 设置
            PmsProductCategory parentCategory = productCategoryMapper.selectByPrimaryKey(productCategory.getParentId());
            if (parentCategory != null) {
                productCategory.setLevel(parentCategory.getLevel() + 1);
            } else {
                productCategory.setLevel(0);
            }
        }
    }

    /**
     * 批量插入商品分类与筛选属性关系表
     * @param productCategoryId 商品分类 id
     * @param productAttributeIdList 相关商品筛选属性 id 集合
     */
    private void insertRelationList(Long productCategoryId, List<Long> productAttributeIdList) {
        List<PmsProductCategoryAttributeRelation> relationList = new ArrayList<>();
        for (Long productAttributeId : productAttributeIdList) {
            PmsProductCategoryAttributeRelation relation = new PmsProductCategoryAttributeRelation();
            relation.setProductCategoryId(productCategoryId);
            relation.setProductAttributeId(productAttributeId);
            relationList.add(relation);
        }
        productCategoryAttributeRelationDAO.insertList(relationList);
    }
}
