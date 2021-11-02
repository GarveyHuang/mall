package com.shura.mall.feignapi.sms;

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
 * @Created: 2021/11/2
 * @Description: 远程调用优惠券服务接口
 */
@FeignClient(name = "mall-coupons", path = "/coupon")
public interface SmsCouponFeignApi {

    @PostMapping("/listCart")
    @ResponseBody
    CommonResult<List<SmsCouponHistoryDetail>> listCart(@RequestParam("type") Integer type,
                                                        @RequestBody List<CartPromotionItem> cartPromotionItemList);
}
