package com.shura.mall.dao.sms;

import com.shura.mall.domain.sms.SmsCouponParam;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券管理自定义查询 DAO
 */
public interface SmsCouponDAO {

    SmsCouponParam getItem(@Param("id") Long id);
}
