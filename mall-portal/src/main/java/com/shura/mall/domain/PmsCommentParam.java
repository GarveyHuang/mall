package com.shura.mall.domain;

import com.shura.mall.model.pms.PmsComment;
import com.shura.mall.model.pms.PmsCommentReply;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description:
 */
@Getter
@Setter
public class PmsCommentParam extends PmsComment {

    private List<PmsCommentReply> pmsCommentReplyList;
}
