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
 * @Created: 2021/11/2
 * @Description: 远程调用会员服务接口
 */
@FeignClient(name = "mall-member", path = "/member")
public interface UmsMemberFeignApi {

    @GetMapping("/address/{id}")
    @ResponseBody
    CommonResult<UmsMemberReceiveAddress> getAddress(@PathVariable("id") Long id);

    @PostMapping("/center/updateUmsMember")
    @ResponseBody
    CommonResult<String> updateMember(@RequestBody UmsMember umsMember);

    @GetMapping("/center/getMemberInfo")
    @ResponseBody
    CommonResult<PortalMemberInfo> getMemberById();

    @GetMapping("/address/list")
    @ResponseBody
    CommonResult<List<UmsMemberReceiveAddress>> list();
}
