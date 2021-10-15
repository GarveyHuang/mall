package com.shura.mall.controller.pms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dto.pms.PmsProductAttributeParam;
import com.shura.mall.dto.pms.ProductAttrInfo;
import com.shura.mall.model.pms.PmsProductAttribute;
import com.shura.mall.service.pms.IPmsProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品属性管理 Controller
 */
@Api(tags = "PmsProductAttributeController", value = "商品属性管理")
@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {

    @Autowired
    private IPmsProductAttributeService productAttributeService;

    @ApiOperation("根据分类查询属性列表或参数列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "type", value = "0表示属性，1表示参数", required = true, paramType = "query", dataType = "integer")})
    @GetMapping(value = "/list/{cid}")
    public CommonResult<CommonPage<PmsProductAttribute>> getList(@PathVariable Long cid,
                                                                 @RequestParam(value = "type") Integer type,
                                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductAttribute> productAttributeList = productAttributeService.getList(cid, type, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productAttributeList));
    }

    @ApiOperation("添加商品属性信息")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody PmsProductAttributeParam productAttributeParam) {
        int count = productAttributeService.create(productAttributeParam);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("修改商品属性信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductAttributeParam productAttributeParam) {
        int count = productAttributeService.update(id, productAttributeParam);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("查询单个商品属性")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsProductAttribute> getItem(@PathVariable Long id) {
        PmsProductAttribute productAttribute = productAttributeService.getItem(id);
        return CommonResult.success(productAttribute);
    }

    @ApiOperation("批量删除商品属性")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = productAttributeService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据商品分类的id获取商品属性及属性分类")
    @GetMapping(value = "/attrInfo/{productCategoryId}")
    public CommonResult<List<ProductAttrInfo>> getAttrInfo(@PathVariable Long productCategoryId) {
        List<ProductAttrInfo> productAttrInfoList = productAttributeService.getProductAttrInfo(productCategoryId);
        return CommonResult.success(productAttrInfoList);
    }
}
