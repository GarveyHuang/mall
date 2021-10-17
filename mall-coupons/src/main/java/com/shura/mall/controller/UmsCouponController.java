package com.shura.mall.controller;

import com.shura.mall.feignapi.OmsCartItemFeignApi;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.domain.SmsCouponHistoryDetail;
import com.shura.mall.model.sms.SmsCouponHistory;
import com.shura.mall.service.IUmsCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 优惠券管理 Controller
 */
@Api(tags = "UmsCouponController", value = "用户优惠券管理")
@RestController
@RequestMapping("/coupon")
public class UmsCouponController {

    @Autowired
    private IUmsCouponService couponService;

    @Autowired
    private OmsCartItemFeignApi omsCartItemClientApi;

    @ApiOperation("领取指定优惠券")
    @PostMapping(value = "/add/{couponId}")
    public CommonResult add(@PathVariable("couponId") Long couponId,
                            @RequestHeader("memberId") Long memberId,
                            @RequestHeader("nickname") String nickname) {
        return couponService.add(couponId, memberId, nickname);
    }

    @ApiOperation("获取用户优惠券列表")
    @ApiImplicitParam(name = "useStatus", value = "优惠券筛选类型：0->未使用；1->已使用；2->已过期",
            allowableValues = "0,1,2", paramType = "query", dataType = "integer")
    @GetMapping(value = "/list")
    public CommonResult<List<SmsCouponHistory>> list(@RequestParam(value = "useStatus", required = false) Integer useStatus,
                                                    @RequestHeader("memberId") Long memberId) {
        return CommonResult.success(couponService.list(useStatus, memberId));
    }

    @ApiOperation("获取用户购物车的优惠券列表")
    @ApiImplicitParam(name = "type", value = "可用类型：0->不可用；1->可用", defaultValue = "1",
            allowableValues = "0,1", paramType = "query", dataType = "integer")
    @GetMapping(value = "/list/cart/{type}")
    public CommonResult<List<SmsCouponHistoryDetail>> listCart(@PathVariable("type") Integer type,
                                                               @RequestHeader("memberId") Long memberId) {
        List<CartPromotionItem> cartPromotionItemList = omsCartItemClientApi.listPromotionByMemberId().getData();
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponService.listCart(cartPromotionItemList, type, memberId);
        return CommonResult.success(couponHistoryDetailList);
    }
}
