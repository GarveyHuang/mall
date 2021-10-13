package com.shura.mall.controller;

import cn.hutool.core.util.StrUtil;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.api.TokenInfo;
import com.shura.mall.service.IUmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 会员登录注册管理 Controller
 */
@Api(tags = "UmsMemberController", value = "会员登录注册管理")
@Slf4j
@RestController
@RequestMapping("/sso")
public class UmsMemberController {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private IUmsMemberService memberService;

    @ApiOperation("会员注册")
    @PostMapping(value = "/register")
    public CommonResult register(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String telephone,
                                 @RequestParam String authCode) {
        return memberService.register(username, password, telephone, authCode);
    }

    @ApiOperation("会员登录")
    @PostMapping(value = "/login")
    public CommonResult login(@RequestParam String username,
                              @RequestParam String password) {
        TokenInfo tokenInfo = memberService.login(username, password);
        if (Objects.isNull(tokenInfo)) {
            return CommonResult.failed("用户名或密码错误");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", tokenInfo.getAccess_token());
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("refreshToken",tokenInfo.getRefresh_token());
        tokenMap.put("memberId",tokenInfo.getAdditionalInfo().get("memberId"));
        tokenMap.put("nickName",username);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/getAuthCode")
    public CommonResult getAuthCode(@RequestParam String telephone) {
        return memberService.generateAuthCode(telephone);
    }

    @ApiOperation("修改密码")
    @PostMapping("/updatePassword")
    public CommonResult updatePassword(@RequestParam String telephone,
                                       @RequestParam String password,
                                       @RequestParam String authCode) {
        return memberService.updatePassword(telephone, password, authCode);
    }

    @ApiOperation("刷新 Token")
    @GetMapping("/refreshToken")
    public CommonResult refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = memberService.refreshToken(token);
        if (StrUtil.isBlank(refreshToken)) {
            return CommonResult.failed("token 已过期！");
        }

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }

    @ApiOperation("获取当前登录用户")
    @PostMapping("/getCurrentMember")
    public CommonResult getCurrentMember() {
        return CommonResult.success(memberService.getCurrentMember());
    }
}
