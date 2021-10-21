package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.PmsCommentParam;
import com.shura.mall.model.pms.PmsComment;
import com.shura.mall.model.pms.PmsCommentReply;
import com.shura.mall.service.IPortalProductCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 商品评论管理 Controller
 */
@Api(tags = "PortalProductCommentController", value = "商品评论管理")
@RestController
@RequestMapping("/portal")
public class PortalProductCommentController {

    @Autowired
    private IPortalProductCommentService portalProductCommentService;

    @ApiOperation("产品评论信息列表")
    @GetMapping(value = "/commentList/{productId}")
    public CommonResult<List<PmsCommentParam>> getCommentList(
            @PathVariable Long productId,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize){
        return portalProductCommentService.getCommentList(productId,pageNum,pageSize);
    }

    @ApiOperation("发布产品评论")
    @PostMapping(value = "/portal/sendComment")
    public CommonResult sendProductComment(@RequestBody PmsComment pmsComment){
        Integer result = portalProductCommentService.insertProductComment(pmsComment);
        if(result > 0){
            return CommonResult.success(null);
        } else if (result == -1){
            return CommonResult.failed("您没有购买过当前商品,无法评价！");
        }

        return CommonResult.failed();
    }

    @ApiOperation("产品评论回复")
    @PostMapping(value = "/portal/sendCommentReply")
    public CommonResult sendProductCommentReply(@RequestBody PmsCommentReply commentReply){
        Integer result = portalProductCommentService.insertCommentReply(commentReply);
        if(result > 0){
            return CommonResult.success(null);
        }

        return CommonResult.failed();
    }
}
