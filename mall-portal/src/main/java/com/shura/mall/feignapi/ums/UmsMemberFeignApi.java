package com.shura.mall.feignapi.ums;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.domain.PortalMemberInfo;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description:
 */
public interface UmsMemberFeignApi {

    @GetMapping(value = "/address/{id}")
    @ResponseBody
    CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable(value = "id") Long id);

    @PostMapping(value = "/center/updateUmsMember")
    @ResponseBody
    CommonResult<String> updateUmsMember(@RequestBody UmsMember umsMember);


    @RequestMapping(value = "/center/getMemberInfo")
    @ResponseBody
    CommonResult<PortalMemberInfo> getMemberById();

    @GetMapping(value = "/address/list")
    @ResponseBody
    CommonResult<List<UmsMemberReceiveAddress>> list();
}
