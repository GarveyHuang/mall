package com.shura.mall.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.sms.SmsCouponDAO;
import com.shura.mall.dao.sms.SmsCouponProductCategoryRelationDAO;
import com.shura.mall.dao.sms.SmsCouponProductRelationDAO;
import com.shura.mall.dto.sms.SmsCouponParam;
import com.shura.mall.mapper.SmsCouponMapper;
import com.shura.mall.mapper.SmsCouponProductCategoryRelationMapper;
import com.shura.mall.mapper.SmsCouponProductRelationMapper;
import com.shura.mall.model.sms.*;
import com.shura.mall.service.sms.ISmsCouponService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券管理 Service 实现类
 */
@Service("couponService")
public class SmsCouponServiceImpl implements ISmsCouponService {

    @Autowired
    private SmsCouponMapper couponMapper;

    @Autowired
    private SmsCouponDAO couponDAO;

    @Autowired
    private SmsCouponProductRelationMapper productRelationMapper;

    @Autowired
    private SmsCouponProductRelationDAO productRelationDAO;

    @Autowired
    private SmsCouponProductCategoryRelationMapper productCategoryRelationMapper;

    @Autowired
    private SmsCouponProductCategoryRelationDAO productCategoryRelationDAO;

    @Override
    public int create(SmsCouponParam couponParam) {
        couponParam.setCount(couponParam.getPublishCount());
        couponParam.setUseCount(0);
        couponParam.setReceiveCount(0);

        //插入优惠券表
        int count = couponMapper.insert(couponParam);

        // 插入优惠券和商品关系表
        if (couponParam.getUseType().equals(2)) {
            for (SmsCouponProductRelation productRelation:couponParam.getProductRelationList()) {
                productRelation.setCouponId(couponParam.getId());
            }
            productRelationDAO.insertList(couponParam.getProductRelationList());
        }

        // 插入优惠券和商品分类关系表
        if (couponParam.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            productCategoryRelationDAO.insertList(couponParam.getProductCategoryRelationList());
        }

        return count;
    }

    @Override
    public int delete(Long id) {
        // 删除优惠券
        int count = couponMapper.deleteByPrimaryKey(id);

        // 删除商品关联
        deleteProductRelation(id);

        // 删除商品分类关联
        deleteProductCategoryRelation(id);

        return count;
    }

    private void deleteProductRelation(Long id) {
        SmsCouponProductCategoryRelationExample productCategoryRelationExample = new SmsCouponProductCategoryRelationExample();
        productCategoryRelationExample.createCriteria().andCouponIdEqualTo(id);
        productCategoryRelationMapper.deleteByExample(productCategoryRelationExample);
    }

    private void deleteProductCategoryRelation(Long id) {
        SmsCouponProductRelationExample productRelationExample = new SmsCouponProductRelationExample();
        productRelationExample.createCriteria().andCouponIdEqualTo(id);
        productRelationMapper.deleteByExample(productRelationExample);
    }

    @Override
    public int update(Long id, SmsCouponParam couponParam) {
        couponParam.setId(id);
        int count = couponMapper.updateByPrimaryKey(couponParam);
        // 删除后插入优惠券和商品关系表
        if (couponParam.getUseType().equals(2)) {
            for(SmsCouponProductRelation productRelation:couponParam.getProductRelationList()){
                productRelation.setCouponId(couponParam.getId());
            }
            deleteProductRelation(id);
            productRelationDAO.insertList(couponParam.getProductRelationList());
        }

        // 删除后插入优惠券和商品分类关系表
        if (couponParam.getUseType().equals(1)) {
            for (SmsCouponProductCategoryRelation couponProductCategoryRelation : couponParam.getProductCategoryRelationList()) {
                couponProductCategoryRelation.setCouponId(couponParam.getId());
            }
            deleteProductCategoryRelation(id);
            productCategoryRelationDAO.insertList(couponParam.getProductCategoryRelationList());
        }
        return count;
    }

    @Override
    public List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum) {
        SmsCouponExample example = new SmsCouponExample();
        SmsCouponExample.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(name)) {
            criteria.andNameLike(name + "%");
        }

        if (type != null) {
            criteria.andTypeEqualTo(type);
        }

        PageHelper.startPage(pageNum,pageSize);
        return couponMapper.selectByExample(example);
    }

    @Override
    public SmsCouponParam getItem(Long id) {
        return couponDAO.getItem(id);
    }
}
