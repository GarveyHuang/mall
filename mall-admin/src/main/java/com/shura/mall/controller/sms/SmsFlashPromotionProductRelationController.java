package com.shura.mall.controller.sms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.sms.SmsFlashPromotionProductResult;
import com.shura.mall.model.sms.SmsFlashPromotionProductRelation;
import com.shura.mall.service.sms.SmsFlashPromotionProductRelationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 限时购和商品关系管理 Controller
 */
@Api(tags = "SmsFlashPromotionProductRelationController", value = "限时购和商品关系管理")
@RestController
@RequestMapping("/flashProductRelation")
public class SmsFlashPromotionProductRelationController {

    @Autowired
    private SmsFlashPromotionProductRelationService flashPromotionProductRelationService;

    @ApiOperation("批量选择商品添加关联")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody List<SmsFlashPromotionProductRelation> relationList) {
        int count = flashPromotionProductRelationService.create(relationList);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("修改关联相关信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody SmsFlashPromotionProductRelation relation) {
        int count = flashPromotionProductRelationService.update(id, relation);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("删除关联")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = flashPromotionProductRelationService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("获取管理商品促销信息")
    @GetMapping(value = "/{id}")
    public CommonResult<SmsFlashPromotionProductRelation> getItem(@PathVariable Long id) {
        SmsFlashPromotionProductRelation relation = flashPromotionProductRelationService.getItem(id);
        return CommonResult.success(relation);
    }

    @ApiOperation("分页查询不同场次关联及商品信息")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsFlashPromotionProductResult>> list(@RequestParam(value = "flashPromotionId") Long flashPromotionId,
                                                                         @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId,
                                                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsFlashPromotionProductResult> flashPromotionProductList = flashPromotionProductRelationService.list(flashPromotionId, flashPromotionSessionId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(flashPromotionProductList));
    }
}
