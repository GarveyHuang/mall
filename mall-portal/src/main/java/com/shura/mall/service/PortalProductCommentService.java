package com.shura.mall.service;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.model.pms.PmsComment;
import com.shura.mall.model.pms.PmsCommentReply;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 商品评论管理 Service
 */
public interface PortalProductCommentService {

    /**
     * 获取评论列表
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    CommonResult getCommentList(Long productId, Integer pageNum, Integer pageSize);

    /**
     * 用户评价
     * @param pmsComment
     * @return
     */
    @Transactional
    Integer insertProductComment(PmsComment pmsComment);

    /**
     * 用户评价回复
     * @param reply
     * @return
     */
    @Transactional
    Integer insertCommentReply(PmsCommentReply reply);
}
