package com.shura.mall.controller.pms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dto.pms.PmsProductCategoryParam;
import com.shura.mall.dto.pms.PmsProductCategoryWithChildrenItem;
import com.shura.mall.model.pms.PmsProductCategory;
import com.shura.mall.service.pms.IPmsProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品分类管理 Controller
 */
@Api(tags = "PmsProductCategoryController", value = "商品分类管理")
@RestController
@RequestMapping("/productCategory")
public class PmsProductCategoryController {

    @Autowired
    private IPmsProductCategoryService productCategoryService;

    @ApiOperation("添加产品分类")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:productCategory:create')")
    public CommonResult create(@Validated @RequestBody PmsProductCategoryParam productCategoryParam) {
        int count = productCategoryService.create(productCategoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
            
        return CommonResult.failed();
    }

    @ApiOperation("修改商品分类")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:productCategory:update')")
    public CommonResult update(@PathVariable Long id,
                               @Validated
                               @RequestBody PmsProductCategoryParam productCategoryParam) {
        int count = productCategoryService.update(id, productCategoryParam);
        if (count > 0) {
            return CommonResult.success(count);
        }
        
        return CommonResult.failed();
    }

    @ApiOperation("分页查询商品分类")
    @GetMapping(value = "/list/{parentId}")
    @PreAuthorize("hasAuthority('pms:productCategory:read')")
    public CommonResult<CommonPage<PmsProductCategory>> getList(@PathVariable Long parentId,
                                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProductCategory> productCategoryList = productCategoryService.getList(parentId, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productCategoryList));
    }

    @ApiOperation("根据id获取商品分类")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('pms:productCategory:read')")
    public CommonResult<PmsProductCategory> getItem(@PathVariable Long id) {
        PmsProductCategory productCategory = productCategoryService.getItem(id);
        return CommonResult.success(productCategory);
    }

    @ApiOperation("删除商品分类")
    @PostMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('pms:productCategory:delete')")
    public CommonResult delete(@PathVariable Long id) {
        int count = productCategoryService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改导航栏显示状态")
    @PostMapping(value = "/update/navStatus")
    @PreAuthorize("hasAuthority('pms:productCategory:update')")
    public CommonResult updateNavStatus(@RequestParam("ids") List<Long> ids, @RequestParam("navStatus") Integer navStatus) {
        int count = productCategoryService.updateNavStatus(ids, navStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改显示状态")
    @PostMapping(value = "/update/showStatus")
    @PreAuthorize("hasAuthority('pms:productCategory:update')")
    public CommonResult updateShowStatus(@RequestParam("ids") List<Long> ids, @RequestParam("showStatus") Integer showStatus) {
        int count = productCategoryService.updateShowStatus(ids, showStatus);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询所有一级分类及子分类")
    @GetMapping(value = "/list/withChildren")
    @PreAuthorize("hasAuthority('pms:productCategory:read')")
    public CommonResult<List<PmsProductCategoryWithChildrenItem>> listWithChildren() {
        List<PmsProductCategoryWithChildrenItem> list = productCategoryService.listWithChildren();
        return CommonResult.success(list);
    }
}
