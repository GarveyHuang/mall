package com.shura.mall.dao.sms;

import com.shura.mall.model.sms.SmsCouponProductRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券和产品关系自定义 DAO
 */
public interface SmsCouponProductRelationDAO {

    int insertList(@Param("list") List<SmsCouponProductRelation> productRelationList);
}
