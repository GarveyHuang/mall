package com.shura.mall.controller;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.base.OssCallbackResult;
import com.shura.mall.domain.base.OssPolicyResult;
import com.shura.mall.service.base.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: OSS 相关操作接口
 */
@Slf4j
@Api(tags = "OssController", value = "Oss 管理")
@RestController
@RequestMapping("/aliyun/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "oss 上传签名生成")
    @GetMapping("/policy")
    public CommonResult<OssPolicyResult> policy() {
        return CommonResult.success(ossService.policy());
    }

    @ApiOperation(value = "上传成功回调")
    @PostMapping("/callback")
    public CommonResult<OssCallbackResult> callback(HttpServletRequest request) {
        log.info("oss callback 回调成功......");
        return CommonResult.success(ossService.callback(request));
    }
}
