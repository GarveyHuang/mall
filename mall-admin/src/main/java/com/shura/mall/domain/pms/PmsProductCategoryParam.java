package com.shura.mall.domain.pms;

import com.shura.mall.validator.FlagValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 添加/更新商品分类的参数
 */
@Getter
@Setter
public class PmsProductCategoryParam {

    @ApiModelProperty("父分类的编号")
    private Long parentId;

    @ApiModelProperty(value = "商品分类名称",required = true)
    @NotBlank(message = "商品分类名称不能为空")
    private String name;

    @ApiModelProperty("分类单位")
    private String productUnit;

    @ApiModelProperty("是否在导航栏显示")
    @FlagValidator(values = {"0","1"}, message = "状态只能为0或1")
    private Integer navStatus;

    @ApiModelProperty("是否进行显示")
    @FlagValidator(values = {"0","1"}, message = "状态只能为0或1")
    private Integer showStatus;

    @ApiModelProperty("排序")
    @Min(value = 0,message = "排序最小为0")
    private Integer sort;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("产品相关筛选属性集合")
    private List<Long> productAttributeIdList;
}
