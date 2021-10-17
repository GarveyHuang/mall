package com.shura.mall.clientapi;

import com.shura.mall.clientapi.interceptor.config.FeignConfig;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.CartPromotionItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 远程调用订单中心购物车详情客户端
 */
@FeignClient(name = "mall-order", configuration = FeignConfig.class)
public interface OmsCartItemClientApi {

    @GetMapping(value = "/cart/list/promotion")
    @ResponseBody
    CommonResult<List<CartPromotionItem>> listPromotionByMemberId();
}
