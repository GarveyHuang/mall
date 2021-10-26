package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.CartProduct;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.model.ums.OmsCartItem;
import com.shura.mall.service.OmsCartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 购物车管理 Controller
 */
@Api(tags = "OmsCartItemController", value = "购物车管理")
@RestController
@RequestMapping("/cart")
public class OmsCartItemController {

    @Autowired
    private OmsCartItemService cartItemService;

    // TODO 这里可以优化，数据不需要都从前端传递
    @ApiOperation(value = "添加商品到购物车")
    @PostMapping("/add")
    public CommonResult add(@RequestBody OmsCartItem cartItem, @RequestHeader("memberId") Long memberId,
                            @RequestHeader("nickname") String nickname) {
        int count = cartItemService.add(cartItem, memberId, nickname);
        if (count > 0) {
            return CommonResult.success(cartItemService.cartItemCount());
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "获取会员的购物车列表")
    @GetMapping("/list")
    public CommonResult<List<OmsCartItem>> list(@RequestHeader("memberId") Long memberId) {
        return CommonResult.success(cartItemService.list(memberId));
    }

    @ApiOperation(value = "获取会员的购物车列表，包括促销信息")
    @GetMapping("/list/promotion")
    public CommonResult<List<CartPromotionItem>> listPromotion(@RequestHeader("memberId") Long memberId) {
        return CommonResult.success(cartItemService.listPromotion(memberId));
    }

    @ApiOperation(value = "修改购物车中某个商品数量")
    @PostMapping("/update/quantity")
    public CommonResult updateQuantity(@RequestParam Long id, @RequestParam Integer quantity,
                                       @RequestHeader("memberId") Long memberId) {
        int count = cartItemService.updateQuantity(id, memberId, quantity);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "获取购物车中某个商品的规格，用于重选规格")
    @GetMapping("/getProduct/{productId}")
    public CommonResult<CartProduct> getCartProduct(@PathVariable("productId") Long productId) {
        return CommonResult.success(cartItemService.getCartProduct(productId));
    }

    @ApiOperation(value = "修改购物车中商品的规格")
    @PostMapping("/update/attr")
    public CommonResult updateAttr(@RequestBody OmsCartItem cartItem,
                                   @RequestHeader("memberId") Long memberId,
                                   @RequestHeader("nickname") String nickname) {
        int count = cartItemService.updateAttr(cartItem, memberId, nickname);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "删除购物车中某个商品")
    @PostMapping("/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids, @RequestHeader("memberId") Long memberId) {
        int count = cartItemService.delete(memberId, ids);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "清空购物车")
    @PostMapping("/clear")
    public CommonResult clear(@RequestHeader("memberId") Long memberId) {
        int count = cartItemService.clear(memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }
}
