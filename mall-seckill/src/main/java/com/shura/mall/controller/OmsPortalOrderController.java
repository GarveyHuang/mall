package com.shura.mall.controller;

import com.alipay.api.internal.util.AlipaySignature;
import com.google.common.collect.Maps;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.common.enums.PayType;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.domain.ConfirmOrderResult;
import com.shura.mall.domain.MqCancelOrder;
import com.shura.mall.domain.OmsOrderDetail;
import com.shura.mall.domain.OrderParam;
import com.shura.mall.service.OmsPortalOrderService;
import com.shura.mall.service.SecKillOrderService;
import com.shura.mall.service.TradeService;
import com.shura.mall.util.RedisOpsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 订单管理 Controller
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "OmsPortalOrderController", value = "订单管理")
public class OmsPortalOrderController {

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Autowired
    private SecKillOrderService secKillOrderService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @ApiOperation(value = "根据购物车信息生成确认单信息", notes = "根据购物车信息生成确认单信息")
    @ApiImplicitParam(name = "itemId", value = "购物车选择购买的选项 id", allowMultiple = true, paramType = "query", dataType = "long")
    @PostMapping("/generateConfirmOrder")
    public CommonResult<ConfirmOrderResult> generateConfirmOrder(@RequestParam("itemIds") List<Long> itemIds,
                                                                 @RequestHeader("memberId") Long memberId) throws BusinessException {
        return CommonResult.success(portalOrderService.generateConfirmOrder(itemIds, memberId));
    }


    @ApiOperation(value = "根据购物车信息生成订单", notes = "根据购物车信息生成订单")
    @PostMapping("/generateOrder")
    public CommonResult generateOrder(@RequestBody OrderParam orderParam,
                                      @RequestHeader("memberId") Long memberId) throws BusinessException {
        return portalOrderService.generateOrder(orderParam, memberId);
    }

    @ApiOperation(value = "秒杀订单确认页", notes = "秒杀订单确认页")
    @PostMapping("/seckill/generateConfirmOrder")
    public CommonResult generateConfirmSecKillOrder(@RequestParam("productId") Long productId,
                                                    @RequestHeader("memberId") Long memberId,
                                                    @RequestHeader("Authorization") String token) throws BusinessException {
        return secKillOrderService.generateConfirmSecKillOrder(productId, memberId, token);
    }

    @ApiOperation(value = "生成秒杀订单", notes = "生成秒杀订单")
    @PostMapping("/seckill/generateOrder")
    public CommonResult generateSecKillOrder(@RequestBody OrderParam orderParam,
                                             @RequestHeader("memberId") Long memberId,
                                             @RequestHeader("Authorization") String token) throws BusinessException {
        return secKillOrderService.generateSecKillOrder(orderParam, memberId, token);
    }

    @ApiOperation(value = "秒杀结果页", notes = "秒杀结果页")
    @GetMapping("/seckill/result")
    public CommonResult secKillResult(@RequestParam("productId") Long productId,
                                      @RequestHeader("memberId") Long memberId) {
        String status = redisOpsUtil.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + memberId + ":" + productId);
        if (StringUtils.isBlank(status)) {
            return CommonResult.success(null, "亲，暂时没有查询到促销订单！");
        }

        if (status.equals("-1")) {
            return CommonResult.success(status, "亲，没有抢购到商品哦，下次请早！");
        }

        if (status.equals("1")) {
            return CommonResult.success(status, "亲，正在排队抢购中，请耐心等待！");
        }

        // 如果 status > 1，则秒杀成功，返回订单编号
        return CommonResult.success(status);
    }

    @ApiOperation(value = "查看订单详情", notes = "查看订单详情")
    @GetMapping("/orderDetail/{orderId}")
    public CommonResult orderDetail(@PathVariable("orderId") Long orderId) {
        return portalOrderService.getDetailOrder(orderId);
    }

    @ApiOperation(value = "支付成功的回调", notes = "支付成功的回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payType", value = "支付方式:0->未支付；1->支付宝支付；2->微信支付",
            allowableValues = "1，2", paramType = "query", dataType = "integer")
    })
    @PostMapping("/paySuccess/{payType}")
    public void paySuccess(@PathVariable("payType") Integer payType,
                           HttpServletRequest request, HttpServletResponse response) throws BusinessException {
        if (payType > PayType.WECHAT_PAY.getType() || payType < PayType.UNPAID.getType()) {
            throw new IllegalArgumentException("支付类型不正确，请检查！");
        }

        if (payType == PayType.ALI_PAY.getType()) {
            // TODO 待测试
//            // 1. 获取 request 里所有与 alipay 相关的参数，封装成一个 map
//            Map<String, String> param = Maps.newHashMap();
//            Enumeration<String> parameterNames = request.getParameterNames();
//            while (parameterNames.hasMoreElements()) {
//                String parameterName = parameterNames.nextElement();
//                log.info("alipay callback parameter:-->" + parameterName + ":->" + request.getParameter(parameterName));
//                if (!parameterName.equals("sign_type")) {
//                    param.put(parameterName, request.getParameter(parameterName));
//                }
//            }
//
//            // 2. 验证请求是否为 alipay 返回的请求内容【验证请求合法性】
//            boolean isPassed = AlipaySignature.rsaCheckV2(param, Configs.getAlipayPublicKey(), StandardCharsets.UTF_8, Configs.getSignType());
//            PrintWriter out = null;
//            try {
//                out = response.getWriter();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (isPassed) {
//                Long orderId = Long.parseLong(param.get("out_trade_no"));
//                int count = portalOrderService.paySuccess(orderId, payType);
//                if (count > 0) {
//                    log.info("支付成功，订单完成支付");
//                    out.print("success");
//                    return;
//                }
//
//                log.info("支付失败，订单未能完成支付");
//                out.print("unSuccess");
//            } else {
//                log.info("支付失败，订单未能完成支付");
//                out.print("unSuccess");
//            }
        } else if (payType == PayType.WECHAT_PAY.getType()) { // 微信支付
            // TODO 暂未实现
        }
    }

    @ApiOperation(value = "自动取消超时订单", notes = "自动取消超时订单")
    @PostMapping("/cancelTimeoutOrder")
    public CommonResult cancelTimeoutOrder() {
        return portalOrderService.cancelTimeoutOrder();
    }

    @ApiOperation(value = "取消单个超时订单", notes = "取消单个超时订单")
    @PostMapping("/cancelOrder/{orderId}")
    public CommonResult cancelOrder(@PathVariable("orderId") Long orderId,
                                    @RequestHeader("memberId") Long memberId) {
        MqCancelOrder mqCancelOrder = new MqCancelOrder();
        mqCancelOrder.setMemberId(memberId);
        mqCancelOrder.setOrderId(orderId);
        portalOrderService.sendDelayMessageCancelOrder(mqCancelOrder);

        return CommonResult.success(null);
    }

    @ApiOperation(value = "删除订单", notes = "删除订单")
    @PostMapping("/deleteOrder/{orderId}")
    public CommonResult deleteOrder(@PathVariable("orderId") Long orderId) {
        int count = portalOrderService.deleteOrder(orderId);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation(value = "查询订单", notes = "查询订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "会员 id", required = true, paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "status", value = "订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
            allowableValues = "0，1，2，3，4", paramType = "query", dataType = "integer")
    })
    @GetMapping("/list/userOrder")
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestHeader("memberId") Long memberId,
            @RequestParam(value = "status", required = false) Integer status) {
        if (memberId == null || (status != null && status > 4)) {
            return CommonResult.validateFailed();
        }

        return portalOrderService.findMemberOrderList(pageSize, pageNum, memberId, status);
    }

    @ApiOperation(value = "订单支付", notes = "订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payType", value = "支付方式：0->未支付；1->支付宝支付；2->微信支付",
            allowableValues = "1,2", paramType = "query", dataType = "integer")})
    @PostMapping("/tradeQrCode/{orderId}")
    public CommonResult tradeQrCode(@PathVariable("orderId") Long orderId,
                                    @RequestHeader("memberId") Long memberId,
                                    @RequestParam("payType") Integer payType) {
        if (payType > PayType.WECHAT_PAY.getType() || payType < PayType.UNPAID.getType()) {
            throw new IllegalArgumentException("支付类型不正确，请检查！");
        }

        // TODO 暂未实现
        return CommonResult.success(null);
    }

    @ApiOperation(value = "订单支付状态查询", notes = "订单支付状态查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payType", value = "支付方式：0->未支付；1->支付宝支付；2->微信支付",
            allowableValues = "1，2", paramType = "query", dataType = "integer")})
    @GetMapping("/tradeStatusQuery/{orderId}}")
    public CommonResult tradeStatusQuery(@PathVariable("orderId") Long orderId,
                                         @RequestParam("payType") Integer payType) {
        if (payType > PayType.WECHAT_PAY.getType() || payType < PayType.UNPAID.getType()) {
            throw new IllegalArgumentException("支付类型不正确，请检查！");
        }

        // TODO 暂未实现
        return CommonResult.success(null);
    }

    /* ------------ 限流防刷部分 --------------- */
    @GetMapping("/token")
    public CommonResult getSecKillToken(HttpServletRequest request,
                                        @RequestParam("productId") Long productId,
                                        @RequestHeader("memberId") Long memberId,
                                        @RequestParam("verifyCode") Integer verifyCode) {
        // TODO 待实现
        return CommonResult.success(null);
    }

    @GetMapping(value="/verifyCode")
    public CommonResult getVerifyCode(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("productId") Long productId,
                                      @RequestHeader("memberId") Long memberId) {
        // TODO 待实现
        return CommonResult.success(null);
    }
}
