package com.shura.mall.service.pms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dto.pms.PmsBrandParam;
import com.shura.mall.mapper.PmsBrandMapper;
import com.shura.mall.mapper.PmsProductMapper;
import com.shura.mall.model.pms.PmsBrand;
import com.shura.mall.model.pms.PmsBrandExample;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductExample;
import com.shura.mall.service.pms.IPmsBrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品品牌 Service 实现类
 */
@Service("brandService")
public class PmsBrandServiceImpl implements IPmsBrandService {

    @Autowired
    private PmsBrandMapper brandMapper;
    @Autowired
    private PmsProductMapper productMapper;

    @Override
    public List<PmsBrand> listAllBrand() {
        return brandMapper.selectByExample(new PmsBrandExample());
    }

    @Override
    public int createBrand(PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);

        // 如果创建时首字母为空，取名称的第一个为首字母
        if (StringUtils.isBlank(pmsBrand.getFirstLetter())) {
            pmsBrand.setFirstLetter(pmsBrand.getName().substring(0, 1));
        }

        return brandMapper.insertSelective(pmsBrand);
    }

    @Override
    public int updateBrand(Long id, PmsBrandParam pmsBrandParam) {
        PmsBrand pmsBrand = new PmsBrand();
        BeanUtils.copyProperties(pmsBrandParam, pmsBrand);
        pmsBrand.setId(id);

        // 更新品牌时，要更新商品中的品牌名称
        PmsProduct pmsProduct = new PmsProduct();
        pmsProduct.setBrandName(pmsBrand.getName());
        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andBrandIdEqualTo(id);
        productMapper.updateByExampleSelective(pmsProduct, example);

        return brandMapper.updateByPrimaryKeySelective(pmsBrand);
    }

    @Override
    public int deleteBrand(Long id) {
        return brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBrand(List<Long> ids) {
        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);
        return brandMapper.deleteByExample(example);
    }

    @Override
    public List<PmsBrand> listBrand(String keyword, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PmsBrandExample example = new PmsBrandExample();
        example.setOrderByClause("sort desc");
        PmsBrandExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike(keyword + "%");
        }

        return brandMapper.selectByExample(example);
    }

    @Override
    public PmsBrand getBrand(Long id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateShowStatus(List<Long> ids, Integer showStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setShowStatus(showStatus);

        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);

        return brandMapper.updateByExampleSelective(pmsBrand, example);
    }

    @Override
    public int updateFactoryStatus(List<Long> ids, Integer factoryStatus) {
        PmsBrand pmsBrand = new PmsBrand();
        pmsBrand.setFactoryStatus(factoryStatus);

        PmsBrandExample example = new PmsBrandExample();
        example.createCriteria().andIdIn(ids);

        return brandMapper.updateByExampleSelective(pmsBrand, example);
    }
}
