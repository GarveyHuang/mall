package com.shura.mall.controller;

import cn.hutool.core.util.StrUtil;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.PortalMemberInfo;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.service.IUmsMemberCenterService;
import com.shura.mall.service.IUmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 会员中心 Controller
 */
@Api(tags = "UmsMemberCenterController", value = "会员中心")
@RestController
@RequestMapping("/member/center")
public class UmsMemberCenterController {

    @Autowired
    private IUmsMemberService memberService;

    @Autowired
    private IUmsMemberCenterService memberCenterService;

    @ApiOperation(value = "用户中心主页相关信息#功能未实现",
            notes = "关注店铺总数，收藏商品总数，近期7天浏览商品数，优惠券数量")
    @GetMapping("/userHome")
    public CommonResult<Map<String, String>> index(){
        return CommonResult.success(null);
    }

    @ApiOperation(value = "获取会员信息", notes = "不包含会员等级信息，会员需要被拆分成微服务")
    @GetMapping("/loadUser")
    public CommonResult<UmsMember> loadUserByUsername(String username){
        UmsMember umsMember = memberService.getByUsername(username);
        if (umsMember == null) {
            return CommonResult.failed("会员不存在或者已经被禁用");
        }

        return CommonResult.success(umsMember);
    }

    @ApiOperation(value = "获取会员详细信息包含会员等级信息")
    @GetMapping("/getMemberInfo")
    public CommonResult<PortalMemberInfo> getMemberInfo(@RequestHeader("memberId") long memberId){
        return CommonResult.success(memberCenterService.getMemberInfo(memberId));
    }

    @ApiOperation(value = "修改会员个人信息")
    @PostMapping(value = "update")
    public CommonResult<String> updateUmsMember(@RequestBody UmsMember umsMember,@RequestHeader("memberId") long memberId){
        if(StrUtil.isNotBlank(umsMember.getPassword())){
            return CommonResult.validateFailed("仅限修改资料,不能修改密码！");
        }

        // 从网关解析 jwt 后 把 memberId 存到请求头中
        umsMember.setId(memberId);

        if(memberService.updateUmsMember(umsMember) > 0){
            return CommonResult.success(null);
        }

        return CommonResult.failed();
    }
}
