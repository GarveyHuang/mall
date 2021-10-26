package com.shura.mall.service.impl;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dao.SmsCouponHistoryDAO;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.domain.SmsCouponHistoryDetail;
import com.shura.mall.mapper.SmsCouponHistoryMapper;
import com.shura.mall.mapper.SmsCouponMapper;
import com.shura.mall.model.sms.*;
import com.shura.mall.service.UmsCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 用户优惠券管理 Service 实现类
 */
@Service("couponService")
public class UmsCouponServiceImpl implements UmsCouponService {

    @Autowired
    private SmsCouponMapper couponMapper;

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Autowired
    private SmsCouponHistoryDAO couponHistoryDAO;

    @Override
    public CommonResult add(Long couponId, Long memberId, String nickName) {
        // 获取优惠券信息
        SmsCoupon coupon = couponMapper.selectByPrimaryKey(couponId);
        if (coupon == null) {
            return CommonResult.failed("优惠券不存在");
        }

        if (coupon.getCount() <= 0) {
            return CommonResult.failed("优惠券已经领完了");
        }

        Date now = new Date();
        if (now.before(coupon.getEnableTime())) {
            return CommonResult.failed("优惠券还没到领取时间");
        }

        // 判断用户领取的优惠券数量是否超过限制
        SmsCouponHistoryExample couponHistoryExample = new SmsCouponHistoryExample();
        couponHistoryExample.createCriteria().andCouponIdEqualTo(couponId).andMemberIdEqualTo(memberId);
        long count = couponHistoryMapper.countByExample(couponHistoryExample);
        if (count >= coupon.getPerLimit()) {
            return CommonResult.failed("您已经领取过该优惠券了");
        }

        // 生成领取优惠券历史
        SmsCouponHistory couponHistory = new SmsCouponHistory();
        couponHistory.setCouponId(couponId);
        couponHistory.setCouponCode(generateCouponCode(memberId));
        couponHistory.setCreateTime(now);
        couponHistory.setMemberId(memberId);
        couponHistory.setMemberNickname(nickName);
        // 主动领取
        couponHistory.setGetType(1);
        // 未使用
        couponHistory.setUseStatus(0);
        couponHistoryMapper.insert(couponHistory);

        // 修改优惠券的数量、领取数量
        coupon.setCount(coupon.getCount() - 1);
        coupon.setReceiveCount(coupon.getReceiveCount() == null? 1 : coupon.getReceiveCount() + 1);
        couponMapper.updateByPrimaryKey(coupon);
        return CommonResult.success(null, "领取成功");
    }

    /**
     * 16 为优惠券码生成：时间戳后 8 位 + 4 位随机数 + 用户 id 后 4 位
     * @param memberId
     * @return
     */
    private String generateCouponCode(Long memberId) {
        StringBuilder sb = new StringBuilder();
        Long currentTimeMills = System.currentTimeMillis();
        String timeMillsStr = currentTimeMills.toString();
        sb.append(timeMillsStr.substring(timeMillsStr.length() - 8));

        for (int i = 0; i < 4; i++) {
            sb.append(new Random().nextInt(10));
        }

        String memberIdStr = memberId.toString();
        if (memberIdStr.length() <= 4) {
            sb.append(String.format("%04d", memberId));
        } else {
            sb.append(memberIdStr.substring(memberIdStr.length() - 4));
        }
        return sb.toString();
    }

    @Override
    public List<SmsCouponHistory> list(Integer useStatus, Long memberId) {
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        SmsCouponHistoryExample.Criteria criteria = example.createCriteria();
        criteria.andMemberIdEqualTo(memberId);

        if (useStatus != null) {
            criteria.andUseStatusEqualTo(useStatus);
        }
        return couponHistoryMapper.selectByExample(example);
    }

    @Override
    public List<SmsCouponHistoryDetail> listCart(List<CartPromotionItem> cartItemList, Integer type, Long memberId) {
        Date now = new Date();
        // 获取当前用户所有优惠券
        List<SmsCouponHistoryDetail> allList = couponHistoryDAO.getDetailList(memberId);
        // 根据优惠券使用类型判断优惠券是否可用
        List<SmsCouponHistoryDetail> enableList = new ArrayList<>();
        List<SmsCouponHistoryDetail> disableList = new ArrayList<>();
        for (SmsCouponHistoryDetail couponHistoryDetail : allList) {
            Integer useType = couponHistoryDetail.getCoupon().getUseType();
            BigDecimal minPoint = couponHistoryDetail.getCoupon().getMinPoint();
            Date endTime = couponHistoryDetail.getCoupon().getEndTime();
            if (useType.equals(0)) {
                // 0 -> 全场通用
                // 判断是否满足优惠起点
                // 计算购物车商品总价
                BigDecimal totalAmount = calcTotalAmount(cartItemList);
                if (now.before(endTime) && totalAmount.subtract(minPoint).intValue() >= 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(1)) {
                // 1 -> 指定分类
                // 计算指定分类商品的总价
                List<Long> productCategoryIds = new ArrayList<>();
                for (SmsCouponProductCategoryRelation categoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                    productCategoryIds.add(categoryRelation.getProductCategoryId());
                }

                BigDecimal totalAmount = calcAmountByProductCategoryId(cartItemList, productCategoryIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() > 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            } else if (useType.equals(2)) {
                // 2 -> 指定商品
                // 计算指定商品的总价
                List<Long> productIds = new ArrayList<>();
                for (SmsCouponProductRelation productRelation : couponHistoryDetail.getProductRelationList()) {
                    productIds.add(productRelation.getProductId());
                }

                BigDecimal totalAmount = calcTotalAmountByProductId(cartItemList, productIds);
                if (now.before(endTime) && totalAmount.intValue() > 0 && totalAmount.subtract(minPoint).intValue() > 0) {
                    enableList.add(couponHistoryDetail);
                } else {
                    disableList.add(couponHistoryDetail);
                }
            }
        }

        if (type.equals(1)) {
            return enableList;
        }

        return disableList;
    }

    private BigDecimal calcTotalAmount(List<CartPromotionItem> cartItemList) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            BigDecimal realPrice = item.getPrice();
            if (null != item.getReduceAmount()) {
                realPrice = item.getPrice().subtract(item.getReduceAmount());
            }

            total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
        }

        return total;
    }

    private BigDecimal calcAmountByProductCategoryId(List<CartPromotionItem> cartItemList, List<Long> productCategoryIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productCategoryIds.contains(item.getProductCategoryId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }

        return total;
    }

    private BigDecimal calcTotalAmountByProductId(List<CartPromotionItem> cartItemList, List<Long> productIds) {
        BigDecimal total = new BigDecimal("0");
        for (CartPromotionItem item : cartItemList) {
            if (productIds.contains(item.getProductId())) {
                BigDecimal realPrice = item.getPrice().subtract(item.getReduceAmount());
                total = total.add(realPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }

        return total;
    }
}
