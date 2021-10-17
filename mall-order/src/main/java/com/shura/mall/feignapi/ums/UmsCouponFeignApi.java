package com.shura.mall.feignapi.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.domain.SmsCouponHistoryDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 会员优惠券服务
 */
@FeignClient(value = "mall-coupons", path = "/coupon")
public interface UmsCouponFeignApi {

    @PostMapping
    @ResponseBody
    CommonResult<List<SmsCouponHistoryDetail>> listCart(@RequestParam("type") Integer type,
                                                        @RequestBody List<CartPromotionItem> cartPromotionItemList);
}