package com.shura.mall.service.pms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.pms.PmsProductAttributeDAO;
import com.shura.mall.domain.pms.PmsProductAttributeParam;
import com.shura.mall.domain.pms.ProductAttrInfo;
import com.shura.mall.mapper.PmsProductAttributeCategoryMapper;
import com.shura.mall.mapper.PmsProductAttributeMapper;
import com.shura.mall.model.pms.PmsProductAttribute;
import com.shura.mall.model.pms.PmsProductAttributeCategory;
import com.shura.mall.model.pms.PmsProductAttributeExample;
import com.shura.mall.service.pms.PmsProductAttributeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品属性 Service 实现类
 */
@Service("productAttributeService")
public class PmsProductAttributeServiceImpl implements PmsProductAttributeService {

    @Autowired
    private PmsProductAttributeMapper productAttributeMapper;

    @Autowired
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;

    @Autowired
    private PmsProductAttributeDAO productAttributeDAO;

    @Override
    public List<PmsProductAttribute> getList(Long cid, Integer type, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.setOrderByClause("sort desc");
        example.createCriteria().andProductAttributeCategoryIdEqualTo(cid).andTypeEqualTo(type);
        return productAttributeMapper.selectByExample(example);
    }

    @Override
    public int create(PmsProductAttributeParam pmsProductAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        BeanUtils.copyProperties(pmsProductAttributeParam, productAttribute);
        int count = productAttributeMapper.insertSelective(productAttribute);

        // 新增商品属性后，需要更新商品属性分类数量
        PmsProductAttributeCategory productAttributeCategory = productAttributeCategoryMapper.selectByPrimaryKey(pmsProductAttributeParam.getProductAttributeCategoryId());
        if (pmsProductAttributeParam.getType() == 0) {
            productAttributeCategory.setAttributeCount(productAttributeCategory.getAttributeCount() + 1);
        } else if (pmsProductAttributeParam.getType() == 1) {
            productAttributeCategory.setParamCount(1);
        }
        productAttributeCategoryMapper.updateByPrimaryKey(productAttributeCategory);

        return count;
    }

    @Override
    public int update(Long id, PmsProductAttributeParam productAttributeParam) {
        PmsProductAttribute productAttribute = new PmsProductAttribute();
        productAttribute.setId(id);
        BeanUtils.copyProperties(productAttributeParam, productAttribute);
        return productAttributeMapper.updateByPrimaryKey(productAttribute);
    }

    @Override
    public PmsProductAttribute getItem(Long id) {
        return productAttributeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        // 获取属性和分类
        PmsProductAttribute productAttribute = productAttributeMapper.selectByPrimaryKey(ids.get(0));
        Integer type = productAttribute.getType();
        PmsProductAttributeCategory productAttributeCategory = productAttributeCategoryMapper.selectByPrimaryKey(productAttribute.getProductAttributeCategoryId());
        PmsProductAttributeExample example = new PmsProductAttributeExample();
        example.createCriteria().andIdIn(ids);
        int count = productAttributeMapper.deleteByExample(example);

        // 删除完成后，修改数量
        if (type == 0) {
            if (productAttributeCategory.getAttributeCount() >= count) {
                productAttributeCategory.setAttributeCount(productAttributeCategory.getAttributeCount() - count);
            } else {
                productAttributeCategory.setAttributeCount(0);
            }
        } else if (type == 1) {
            if (productAttributeCategory.getParamCount() >= count) {
                productAttributeCategory.setParamCount(productAttributeCategory.getParamCount() - count);
            } else {
                productAttributeCategory.setParamCount(0);
            }
        }
        productAttributeCategoryMapper.updateByPrimaryKey(productAttributeCategory);
        return 0;
    }

    @Override
    public List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId) {
        return productAttributeDAO.getProductAttrInfo(productCategoryId);
    }
}
