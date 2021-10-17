package com.shura.mall.feignapi.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.PortalMemberInfo;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 远程调用会员中心获取具体收货地址
 */
@FeignClient(name = "mall-member", path = "/member")
public interface UmsMemberFeignApi {

    @GetMapping(value = "/address/{id}")
    @ResponseBody
    CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable(value = "id") Long id);

    @PostMapping(value = "/center/updateUmsMember")
    @ResponseBody
    CommonResult<String> updateUmsMember(@RequestBody UmsMember umsMember);


    @GetMapping(value = "/center/getMemberInfo")
    @ResponseBody
    CommonResult<PortalMemberInfo> getMemberById();

    @GetMapping(value = "/address/list")
    @ResponseBody
    CommonResult<List<UmsMemberReceiveAddress>> list();
}
