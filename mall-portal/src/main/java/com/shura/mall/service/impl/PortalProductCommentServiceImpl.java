package com.shura.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.dao.PortalProductCommentDAO;
import com.shura.mall.feignapi.ums.UmsMemberFeignApi;
import com.shura.mall.mapper.PmsCommentMapper;
import com.shura.mall.mapper.PmsCommentReplyMapper;
import com.shura.mall.model.pms.PmsComment;
import com.shura.mall.model.pms.PmsCommentReply;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.service.PortalProductCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 商品评论管理 Service 实现类
 */
@Service("portalProductCommentService")
public class PortalProductCommentServiceImpl implements PortalProductCommentService {

    @Autowired
    private PortalProductCommentDAO productCommentDAO;

    @Autowired
    private PmsCommentMapper pmsMapper;

    @Autowired
    private PmsCommentReplyMapper replyMapper;

    @Autowired
    private UmsMemberFeignApi umsMemberFeignApi;

    @Override
    public CommonResult getCommentList(Long productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return CommonResult.success(productCommentDAO.getCommentList(productId));
    }

    @Override
    public Integer insertProductComment(PmsComment pmsComment) {
        UmsMember member = umsMemberFeignApi.getMemberById().getData();
        // 判断一下当前用户是否购买过当前评论的商品
        Integer status = productCommentDAO.selectUserOrder(member.getId(), pmsComment.getProductId());
        if(status > 0){
            pmsComment.setCreateTime(new Date());
            pmsComment.setShowStatus(0);
            pmsComment.setMemberNickName(member.getNickname());
            pmsComment.setMemberIcon(member.getIcon());
            return pmsMapper.insert(pmsComment);
        }
        return -1;
    }

    @Override
    public Integer insertCommentReply(PmsCommentReply reply) {
        UmsMember member = umsMemberFeignApi.getMemberById().getData();
        reply.setCreateTime(new Date());
        reply.setMemberNickName(member.getNickname());
        reply.setMemberIcon(member.getIcon());
        reply.setType(0);
        return replyMapper.insert(reply);
    }
}
