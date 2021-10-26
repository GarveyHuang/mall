package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.domain.ConfirmOrderResult;
import com.shura.mall.domain.MqCancelOrder;
import com.shura.mall.domain.OmsOrderDetail;
import com.shura.mall.domain.OrderParam;
import com.shura.mall.service.OmsPortalOrderService;
import com.shura.mall.util.RedisOpsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 订单管理 Controller
 */
@Slf4j
@Api(tags = "OmsPortalOrderController", value = "订单管理")
@RestController
@RequestMapping("/order")
public class OmsPortalOrderController {

    @Autowired
    private OmsPortalOrderService portalOrderService;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @ApiOperation("根据购物车信息生成确认单信息")
    @ApiImplicitParam(name = "itemId", value = "购物车选择购买的选项ID", allowMultiple = true, paramType = "query", dataType = "long")
    @PostMapping(value = "/generateConfirmOrder")
    public CommonResult<ConfirmOrderResult> generateConfirmOrder(@RequestParam("itemIds") List<Long> itemIds,
                                                                 @RequestHeader("memberId") Long memberId) throws BusinessException {
        ConfirmOrderResult confirmOrderResult = portalOrderService.generateConfirmOrder(itemIds, memberId);
        return CommonResult.success(confirmOrderResult);
    }

    @ApiOperation("根据购物车信息生成订单")
    @PostMapping(value = "/generateOrder")
    public CommonResult generateOrder(@RequestBody OrderParam orderParam,
                                      @RequestHeader("memberId") Long memberId) throws BusinessException {
        return portalOrderService.generateOrder(orderParam,memberId);
    }

    // TODO 秒杀功能暂未实现

    @ApiOperation("秒杀订单确认#功能尚未实现")
    @PostMapping(value = "/miaosha/generateConfirmOrder")
    @ResponseBody
    public CommonResult generateMiaoShaConfirmOrder(@RequestParam("productId") Long productId,
                                                    String token,
                                                    @RequestHeader("memberId") Long memberId) throws BusinessException {
        return CommonResult.success(null);
    }

    @ApiOperation("秒杀订单创建#功能尚未实现")
    @PostMapping(value = "/miaosha/generateOrder")
    @ResponseBody
    public CommonResult generateMiaoShaOrder(@RequestBody OrderParam orderParam,
                                             String token,
                                             @RequestHeader("memberId") Long memberId) throws BusinessException {
        return CommonResult.success(null);
    }

    @ApiOperation("秒杀结果详情#功能尚未实现")
    @GetMapping("/miaosha/result")
    public CommonResult miaoShaResult(@RequestParam("productId") Long productId,@RequestHeader("memberId") Long memberId){
        return CommonResult.success(null);
    }

    @ApiOperation("查看订单详情")
    @GetMapping(value = "/orderDetail")
    public CommonResult orderDetail(@RequestParam Long orderId){
        return portalOrderService.getDetailOrder(orderId);
    }

    @ApiOperation("支付成功的回调#功能尚未实现")
    @ApiImplicitParams({@ApiImplicitParam(name = "payType", value = "支付方式:0->未支付，1->支付宝支付，2->微信支付",
            allowableValues = "1,2", paramType = "query", dataType = "integer")})
    @PostMapping(value = "/paySuccess/{payType}")
    public void paySuccess(@PathVariable Integer payType,
                           HttpServletRequest request,
                           HttpServletResponse response) {
        if(payType > 2 || payType < 0) {
            throw new IllegalArgumentException("支付类型不正确，平台目前仅支持支付宝与微信支付");
        }

        // TODO 功能尚未实现
    }

    @ApiOperation("自动取消超时订单")
    @PostMapping(value = "/cancelTimeOutOrder")
    public CommonResult cancelTimeOutOrder(){
        return portalOrderService.cancelTimeOutOrder();
    }

    @ApiOperation("取消单个超时订单")
    @PostMapping(value = "/cancelOrder")
    public CommonResult cancelOrder(Long orderId,@RequestHeader("memberId") Long memberId) {
        MqCancelOrder mqCancelOrder = new MqCancelOrder();
        mqCancelOrder.setMemberId(memberId);
        mqCancelOrder.setOrderId(orderId);

        portalOrderService.sendDelayMessageCancelOrder(mqCancelOrder);
        return CommonResult.success(null);
    }

    /**
     * 删除订单[逻辑删除],只能 status 为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     */
    @ApiOperation(value = "删除会员订单#杨过添加", notes = "status为：3->已完成；4->已关闭；5->无效订单，才可以删除，否则只能先取消订单然后删除")
    @PostMapping(value = "/deleteOrder")
    public CommonResult deleteOrder(Long orderId){
        int total = portalOrderService.deleteOrder(orderId);
        if(total > 0) {
            return CommonResult.success("有：" + total + "：条订单被删除");
        }

        return CommonResult.failed("订单已经被删除或者没有符合条件的订单");
    }

    /**
     * 订单服务由会员服务调用，会员服务传来会员 id
     *
     * @param memberId
     * @param status   null 查询所有
     *                 订单状态：0->待付款；1->待发货；2->已发货；3->已完成;4->已关闭；
     * @return
     */
    @ApiOperation("会员订单查询#杨过添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "会员ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "status", value = "订单状态:0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭",
                    allowableValues = "0,1,2,3,4", paramType = "query", dataType = "integer")})
    @PostMapping(value = "/list/userOrder")
    @ResponseBody
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "memberId") Long memberId,
            @RequestParam(value = "status", required = false) Integer status) {

        if (memberId == null || (status != null && status > 4)) {
            return CommonResult.validateFailed();
        }
        return portalOrderService.findMemberOrderList(pageSize, pageNum, memberId, status);
    }

    /**
     * 订单支付逻辑：支付支持两种方式：alipay, wechat
     * @param orderId
     * @param payType
     * @return
     */
    @ApiOperation("订单支付#功能暂未实现}")
    @ApiImplicitParams({@ApiImplicitParam(name = "payType", value = "支付方式:0->未支付,1->支付宝支付,2->微信支付",
            allowableValues = "1,2", paramType = "query", dataType = "integer")})
    @RequestMapping(value = "/tradeQrCode",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult tradeQrCode(@RequestParam(value = "orderId") Long orderId,
                                    @RequestHeader("memberId") Long memberId,
                                    @RequestParam(value = "payType") Integer payType){
        if(payType > 2 || payType < 0) {
            throw new IllegalArgumentException("支付类型不正确，平台目前仅支持支付宝与微信支付");
        }
        return CommonResult.success(null);
    }


    @ApiOperation("订单支付状态查询,手动查询#功能尚未实现")
    @ApiImplicitParams({@ApiImplicitParam(name = "payType", value = "支付方式:0->未支付,1->支付宝支付,2->微信支付",
            allowableValues = "1,2", paramType = "query", dataType = "integer")})
    @PostMapping(value = "/tradeStatusQuery")
    @ResponseBody
    public CommonResult tradeStatusQuery(@RequestParam(value = "orderId") Long orderId,
                                         @RequestParam(value = "payType") Integer payType){

        if(payType > 2 || payType < 0) {
            throw new IllegalArgumentException("支付类型不正确，平台目前仅支持支付宝与微信支付");
        }
        return CommonResult.success(null);
    }

    // TODO 功能尚未实现
    /*-------------------------------- 限流防刷部分 ---------------------------------------*/
    @GetMapping(value="/token")
    public CommonResult getMiaoshaToken(
            HttpServletRequest request,
            @RequestParam("productId") Long productId,
            @RequestHeader("memberId") Long memberId,
            @RequestParam Integer verifyCode) {
        return CommonResult.success(null);
    }

    @GetMapping(value="/verifyCode")
    @ResponseBody
    public CommonResult getVerifyCode(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestParam("productId") Long productId,
                                      @RequestHeader("memberId") Long memberId){
            return CommonResult.failed("秒杀失败");
    }
}
