package com.shura.mall.controller.pms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dto.pms.PmsProductParam;
import com.shura.mall.dto.pms.PmsProductQueryParam;
import com.shura.mall.dto.pms.PmsProductResult;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.service.pms.IPmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品管理 Controller
 */
@Api(tags = "PmsProductController", value = "商品管理")
@RestController
@RequestMapping("/product")
public class PmsProductController {

    @Autowired
    private IPmsProductService productService;

    @ApiOperation("创建商品")
    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('pms:product:create')")
    public CommonResult create(@RequestBody PmsProductParam productParam) {
        int count = productService.create(productParam);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("根据商品id获取商品编辑信息")
    @GetMapping(value = "/updateInfo/{id}")
    @PreAuthorize("hasAuthority('pms:product:read')")
    public CommonResult<PmsProductResult> getUpdateInfo(@PathVariable Long id) {
        PmsProductResult productResult = productService.getUpdateInfo(id);
        return CommonResult.success(productResult);
    }

    @ApiOperation("更新商品")
    @PostMapping(value = "/update/{id}")
    @PreAuthorize("hasAuthority('pms:product:update')")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductParam productParam, BindingResult bindingResult) {
        int count = productService.update(id, productParam);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("查询商品")
    @GetMapping(value = "/list")
    @PreAuthorize("hasAuthority('pms:product:read')")
    public CommonResult<CommonPage<PmsProduct>> getList(@RequestBody PmsProductQueryParam productQueryParam,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<PmsProduct> productList = productService.list(productQueryParam, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(productList));
    }

    @ApiOperation("根据商品名称或货号模糊查询")
    @GetMapping(value = "/simpleList")
    public CommonResult<List<PmsProduct>> getList(String keyword) {
        List<PmsProduct> productList = productService.list(keyword);
        return CommonResult.success(productList);
    }

    @ApiOperation("批量修改审核状态")
    @PostMapping(value = "/update/verifyStatus")
    @PreAuthorize("hasAuthority('pms:product:update')")
    public CommonResult updateVerifyStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("verifyStatus") Integer verifyStatus,
                                           @RequestParam("detail") String detail) {
        int count = productService.updateVerifyStatus(ids, verifyStatus, detail);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("批量上下架")
    @PostMapping(value = "/update/publishStatus")
    @PreAuthorize("hasAuthority('pms:product:update')")
    public CommonResult updatePublishStatus(@RequestParam("ids") List<Long> ids,
                                            @RequestParam("publishStatus") Integer publishStatus) {
        int count = productService.updatePublishStatus(ids, publishStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("批量推荐商品")
    @PostMapping(value = "/update/recommendStatus")
    @PreAuthorize("hasAuthority('pms:product:update')")
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids,
                                              @RequestParam("recommendStatus") Integer recommendStatus) {
        int count = productService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("批量设为新品")
    @PostMapping(value = "/update/newStatus")
    @PreAuthorize("hasAuthority('pms:product:update')")
    public CommonResult updateNewStatus(@RequestParam("ids") List<Long> ids,
                                        @RequestParam("newStatus") Integer newStatus) {
        int count = productService.updateNewStatus(ids, newStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }

    @ApiOperation("批量修改删除状态")
    @PostMapping(value = "/update/deleteStatus")
    @PreAuthorize("hasAuthority('pms:product:delete')")
    public CommonResult updateDeleteStatus(@RequestParam("ids") List<Long> ids,
                                           @RequestParam("deleteStatus") Integer deleteStatus) {
        int count = productService.updateDeleteStatus(ids, deleteStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }

        return CommonResult.failed();
    }
}
