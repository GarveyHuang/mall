package com.shura.mall.model.ums;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;

public class UmsMemberLevel implements Serializable {
    private Long id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "成长积分")
    private Integer growthPoint;

    @ApiModelProperty(value = "是否为默认等级：0->不是；1->是")
    private Integer defaultStatus;

    @ApiModelProperty(value = "免运费标准")
    private BigDecimal freeFreightPoint;

    @ApiModelProperty(value = "每次评价获取的成长值")
    private Integer commentGrowthPoint;

    @ApiModelProperty(value = "是否有免邮特权")
    private Integer privilegeFreeFreight;

    @ApiModelProperty(value = "是否有签到特权")
    private Integer privilegeSignIn;

    @ApiModelProperty(value = "是否有评论获奖励特权")
    private Integer privilegeComment;

    @ApiModelProperty(value = "是否有专享活动特权")
    private Integer privilegePromotion;

    @ApiModelProperty(value = "是否有会员价格特权")
    private Integer privilegeMemberPrice;

    @ApiModelProperty(value = "是否有生日特权")
    private Integer privilegeBirthday;

    @ApiModelProperty(value = "备注信息")
    private String note;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrowthPoint() {
        return growthPoint;
    }

    public void setGrowthPoint(Integer growthPoint) {
        this.growthPoint = growthPoint;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public BigDecimal getFreeFreightPoint() {
        return freeFreightPoint;
    }

    public void setFreeFreightPoint(BigDecimal freeFreightPoint) {
        this.freeFreightPoint = freeFreightPoint;
    }

    public Integer getCommentGrowthPoint() {
        return commentGrowthPoint;
    }

    public void setCommentGrowthPoint(Integer commentGrowthPoint) {
        this.commentGrowthPoint = commentGrowthPoint;
    }

    public Integer getPrivilegeFreeFreight() {
        return privilegeFreeFreight;
    }

    public void setPrivilegeFreeFreight(Integer privilegeFreeFreight) {
        this.privilegeFreeFreight = privilegeFreeFreight;
    }

    public Integer getPrivilegeSignIn() {
        return privilegeSignIn;
    }

    public void setPrivilegeSignIn(Integer privilegeSignIn) {
        this.privilegeSignIn = privilegeSignIn;
    }

    public Integer getPrivilegeComment() {
        return privilegeComment;
    }

    public void setPrivilegeComment(Integer privilegeComment) {
        this.privilegeComment = privilegeComment;
    }

    public Integer getPrivilegePromotion() {
        return privilegePromotion;
    }

    public void setPrivilegePromotion(Integer privilegePromotion) {
        this.privilegePromotion = privilegePromotion;
    }

    public Integer getPrivilegeMemberPrice() {
        return privilegeMemberPrice;
    }

    public void setPrivilegeMemberPrice(Integer privilegeMemberPrice) {
        this.privilegeMemberPrice = privilegeMemberPrice;
    }

    public Integer getPrivilegeBirthday() {
        return privilegeBirthday;
    }

    public void setPrivilegeBirthday(Integer privilegeBirthday) {
        this.privilegeBirthday = privilegeBirthday;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", growthPoint=").append(growthPoint);
        sb.append(", defaultStatus=").append(defaultStatus);
        sb.append(", freeFreightPoint=").append(freeFreightPoint);
        sb.append(", commentGrowthPoint=").append(commentGrowthPoint);
        sb.append(", privilegeFreeFreight=").append(privilegeFreeFreight);
        sb.append(", privilegeSignIn=").append(privilegeSignIn);
        sb.append(", privilegeComment=").append(privilegeComment);
        sb.append(", privilegePromotion=").append(privilegePromotion);
        sb.append(", privilegeMemberPrice=").append(privilegeMemberPrice);
        sb.append(", privilegeBirthday=").append(privilegeBirthday);
        sb.append(", note=").append(note);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}