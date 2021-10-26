package com.shura.mall.controller.cms;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.cms.CmsPreferenceArea;
import com.shura.mall.service.cms.CmsPreferenceAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 商品优选管理 Controller
 */
@Api(tags = "CmsPreferenceAreaController", value = "商品优选管理")
@RestController
@RequestMapping("/preferenceArea")
public class CmsPreferenceAreaController {

    @Autowired
    private CmsPreferenceAreaService preferenceAreaService;

    @ApiOperation("获取所有商品优选")
    @GetMapping(value = "/listAll")
    public CommonResult<List<CmsPreferenceArea>> listAll() {
        List<CmsPreferenceArea> preferenceAreaList = preferenceAreaService.listAll();
        return CommonResult.success(preferenceAreaList);
    }
}
