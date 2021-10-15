package com.shura.mall.controller.pms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dao.pms.PmsProductAttributeCategoryItem;
import com.shura.mall.model.pms.PmsProductAttributeCategory;
import com.shura.mall.service.pms.IPmsProductAttributeCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品属性分类 Controller
 */
@Api(tags = "PmsProductAttributeCategoryController", value = "商品属性分类")
@RestController
@RequestMapping("/productAttribute/category")
public class PmsProductAttributeCategoryController {

    @Autowired
    private IPmsProductAttributeCategoryService productAttributeCategoryService;

    @ApiOperation("添加商品属性分类")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestParam String name) {
        int count = productAttributeCategoryService.create(name);
        if (count > 0) {
            return CommonResult.success(count);
        }
            
        return CommonResult.failed();
    }

    @ApiOperation("修改商品属性分类")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestParam String name) {
        int count = productAttributeCategoryService.update(id, name);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("删除单个商品属性分类")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = productAttributeCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("获取单个商品属性分类信息")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsProductAttributeCategory> getItem(@PathVariable Long id) {
        PmsProductAttributeCategory productAttributeCategory = productAttributeCategoryService.getItem(id);
        return CommonResult.success(productAttributeCategory);
    }

    @ApiOperation("分页获取所有商品属性分类")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<PmsProductAttributeCategory>> getList(@RequestParam(defaultValue = "5") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum) {
        List<PmsProductAttributeCategory> productAttributeCategoryList = productAttributeCategoryService.getList(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productAttributeCategoryList));
    }

    @ApiOperation("获取所有商品属性分类及其下属性")
    @GetMapping(value = "/list/withAttr")
    public CommonResult<List<PmsProductAttributeCategoryItem>> getListWithAttr() {
        List<PmsProductAttributeCategoryItem> productAttributeCategoryResultList = productAttributeCategoryService.getListWithAttr();
        return CommonResult.success(productAttributeCategoryResultList);
    }
}
