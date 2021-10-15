package com.shura.mall.controller.sms;

import com.shura.mall.common.api.CommonPage;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.sms.SmsHomeRecommendSubject;
import com.shura.mall.service.sms.ISmsHomeRecommendSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 首页专题推荐管理 Controller
 */
@Api(tags = "SmsHomeRecommendSubjectController", value = "首页专题推荐管理")
@RestController
@RequestMapping("/home/recommendSubject")
public class SmsHomeRecommendSubjectController {
    
    @Autowired
    private ISmsHomeRecommendSubjectService homeRecommendSubjectService;

    @ApiOperation("添加首页推荐专题")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody List<SmsHomeRecommendSubject> homeBrandList) {
        int count = homeRecommendSubjectService.create(homeBrandList);
        if (count > 0) {
            return CommonResult.success(count);
        }
        
        return CommonResult.failed();
    }

    @ApiOperation("修改推荐排序")
    @PostMapping(value = "/update/sort/{id}")
    public CommonResult updateSort(@PathVariable Long id, Integer sort) {
        int count = homeRecommendSubjectService.updateSort(id, sort);
        if (count > 0) {
            return CommonResult.success(count);
        }
        
        return CommonResult.failed();
    }

    @ApiOperation("批量删除推荐")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = homeRecommendSubjectService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        
        return CommonResult.failed();
    }

    @ApiOperation("批量修改推荐状态")
    @PostMapping(value = "/update/recommendStatus")
    public CommonResult updateRecommendStatus(@RequestParam("ids") List<Long> ids, @RequestParam Integer recommendStatus) {
        int count = homeRecommendSubjectService.updateRecommendStatus(ids, recommendStatus);
        if (count > 0) {
            return CommonResult.success(count);
        }
        
        return CommonResult.failed();
    }

    @ApiOperation("分页查询推荐")
    @GetMapping(value = "/list")
    public CommonResult<CommonPage<SmsHomeRecommendSubject>> list(@RequestParam(value = "subjectName", required = false) String subjectName,
                                                                  @RequestParam(value = "recommendStatus", required = false) Integer recommendStatus,
                                                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<SmsHomeRecommendSubject> homeBrandList = homeRecommendSubjectService.list(subjectName, recommendStatus, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(homeBrandList));
    }
}
