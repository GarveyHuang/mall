package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dao.PortalProductDAO;
import com.shura.mall.domain.*;
import com.shura.mall.service.IPmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 商品详情信息管理 Controller
 */
@Api(tags = "PortalProductController", value = "商品详情信息管理")
@RestController
@RequestMapping("/pms")
public class PortalProductController {

    @Autowired
    private IPmsProductService productService;

    @Autowired
    private PortalProductDAO portalProductDAO;

    // TODO 以下的功能都需要根据 QPS 做优化

    @ApiOperation(value = "根据商品id获取商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "秒杀活动ID", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "flashPromotionSessionId", value = "活动场次ID,例如:12点场", paramType = "query", dataType = "long")
    })
    @GetMapping(value = "/productInfo/{id}")
    public CommonResult getProductInfo(@PathVariable Long id) {
        PmsProductParam pmsProductParam= productService.getProductInfo(id);
        return CommonResult.success(pmsProductParam);
    }




    @ApiOperation(value = "根据商品Id获取购物车商品的信息")
    @GetMapping(value = "/cartProduct/{productId}")
    public CommonResult<CartProduct> getCartProduct(@PathVariable("productId") Long productId){
        CartProduct cartProduct = portalProductDAO.getCartProduct(productId);
        return CommonResult.success(cartProduct);
    }

    @ApiOperation(value = "根据商品Ids获取促销商品信息")
    @GetMapping(value = "/getPromotionProductList")
    public CommonResult<List<PromotionProduct>> getPromotionProductList(@RequestParam("productIds") List<Long> ids){
        List<PromotionProduct> promotionProducts = portalProductDAO.getPromotionProductList(ids);
        return CommonResult.success(promotionProducts);
    }

    @ApiOperation("当前秒杀活动场-产品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "秒杀活动ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "flashPromotionSessionId", value = "秒杀活动时间段ID", required = true, paramType = "query", dataType = "integer")})
    @GetMapping("/flashPromotion/productList")
    public CommonResult<List<FlashPromotionProduct>> getProduct(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            //当前秒杀活动主题ID
            @RequestParam(value = "flashPromotionId") Long flashPromotionId,
            //当前秒杀活动场次ID
            @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId){
        return CommonResult.success(productService.getFlashProductList(pageSize,pageNum,flashPromotionId,flashPromotionSessionId));
    }


    @ApiOperation(value = "获取当前日期所有活动场次",notes = "示例：10:00 场，13:00 场")
    @GetMapping("/flashPromotion/getSessionTimeList")
    public CommonResult<List<FlashPromotionSessionExt>> getSessionTimeList() {
        return CommonResult.success(productService.getFlashPromotionSessionList());
    }

    /**
     * 获取首页秒杀商品
     * @return
     */
    @GetMapping("/flashPromotion/getHomeSecKillProductList")
    public CommonResult<List<FlashPromotionProduct>> getHomeSecKillProductList(){
        return CommonResult.success(productService.getHomeSecKillProductList());
    }
}
