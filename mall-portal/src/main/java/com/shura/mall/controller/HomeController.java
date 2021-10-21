package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.HomeContentResult;
import com.shura.mall.model.cms.CmsSubject;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsProductCategory;
import com.shura.mall.service.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 首页内容管理 Controller
 */
@Api(tags = "HomeController", value = "首页内容管理")
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private IHomeService homeService;

    @ApiOperation("首页内容页信息展示")
    @GetMapping(value = "/content")
    public CommonResult<HomeContentResult> content() {
        HomeContentResult contentResult = homeService.content();
        return CommonResult.success(contentResult);
    }

    @ApiOperation("分页获取推荐商品")
    @GetMapping(value = "/recommendProductList")
    public CommonResult<List<PmsProduct>> recommendProductList(@RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = homeService.recommendProductList(pageSize, pageNum);
        return CommonResult.success(productList);
    }

    @ApiOperation("获取首页商品分类")
    @GetMapping(value = "/productCateList/{parentId}")
    public CommonResult<List<PmsProductCategory>> getProductCateList(@PathVariable Long parentId) {
        List<PmsProductCategory> productCategoryList = homeService.getProductCateList(parentId);
        return CommonResult.success(productCategoryList);
    }

    @ApiOperation("根据分类获取专题")
    @GetMapping(value = "/subjectList")
    public CommonResult<List<CmsSubject>> getSubjectList(@RequestParam(required = false) Long cateId,
                                                         @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<CmsSubject> subjectList = homeService.getSubjectList(cateId,pageSize,pageNum);
        return CommonResult.success(subjectList);
    }
}
