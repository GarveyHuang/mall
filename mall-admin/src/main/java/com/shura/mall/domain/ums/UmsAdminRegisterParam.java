package com.shura.mall.domain.ums;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: 用户注册参数
 */
@Getter
@Setter
public class UmsAdminRegisterParam {

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度只能输入 6-20 位")
    private String password;

    @ApiModelProperty(value = "邮箱")
    @Email(message = "邮箱格式不合法")
    private String email;

    @ApiModelProperty(value = "用户头像")
    private String icon;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "备注")
    private String note;
}
