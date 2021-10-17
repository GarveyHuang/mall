package com.shura.mall.dao;

import com.shura.mall.domain.SmsCouponHistoryDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 会员优惠券领取历史自定义 DAO
 */
public interface SmsCouponHistoryDAO {

    List<SmsCouponHistoryDetail> getDetailList(@Param("memberId") Long memberId);
}
