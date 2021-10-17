package com.shura.mall.feignapi.pms;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.CartProduct;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.domain.PmsProductParam;
import com.shura.mall.domain.PromotionProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 调用商品服务接口
 */
@FeignClient(name = "mall-product")
public interface PmsProductFeignApi {

    @GetMapping(value = "/pms/cartProduct/{productId}")
    @ResponseBody
    CommonResult<CartProduct> getCartProduct(@PathVariable("productId") Long productId);

    @GetMapping(value = "/pms/getPromotionProductList")
    CommonResult<List<PromotionProduct>> getPromotionProductList(@RequestParam("productIds") List<Long> ids);

    @GetMapping("/stock/lockStock")
    CommonResult lockStock(@RequestBody List<CartPromotionItem> cartPromotionItemList);

    @GetMapping(value = "/pms/productInfo/{id}")
    @ResponseBody
    CommonResult<PmsProductParam> getProductInfo(@PathVariable("id") Long id);

    @GetMapping(value = "/stock/selectStock")
    @ResponseBody
    CommonResult<Integer> selectStock(@RequestParam("productId") Long productId,
                                      @RequestParam(value = "flashPromotionRelationId") Long flashPromotionRelationId);
}
