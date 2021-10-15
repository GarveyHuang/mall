package com.shura.mall.dao.sms;

import com.shura.mall.model.sms.SmsCouponProductCategoryRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券和商品分类关系自定义 DAO
 */
public interface SmsCouponProductCategoryRelationDAO {

    int insertList(@Param("list") List<SmsCouponProductCategoryRelation> productCategoryRelationList);
}
