package com.shura.mall.dto.pms;

import com.shura.mall.validator.FlagValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 品牌传递参数
 */
@Getter
@Setter
public class PmsBrandParam {

    @ApiModelProperty(value = "品牌名称", required = true)
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "品牌首字母")
    private String firstLetter;

    @ApiModelProperty(value = "排序字段")
    @Min(value = 0, message = "排序最小为 0")
    private Integer sort;

    @ApiModelProperty(value = "是否为厂家制造商")
    @FlagValidator(values = {"0", "1"}, message = "厂家状态不正确")
    private Integer factoryStatus;

    @ApiModelProperty(value = "是否进行显示")
    @FlagValidator(values = {"0", "1"}, message = "显示状态不正确")
    private Integer showStatus;

    @ApiModelProperty(value = "品牌 logo", required = true)
    @NotBlank(message = "品牌 logo 不能为空")
    private String logo;

    @ApiModelProperty(value = "品牌大图")
    private String bigPic;

    @ApiModelProperty(value = "品牌故事")
    private String brandStory;
}
