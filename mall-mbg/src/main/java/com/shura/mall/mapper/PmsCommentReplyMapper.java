package com.shura.mall.mapper;

import com.shura.mall.model.pms.PmsCommentReply;
import com.shura.mall.model.pms.PmsCommentReplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsCommentReplyMapper {
    long countByExample(PmsCommentReplyExample example);

    int deleteByExample(PmsCommentReplyExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsCommentReply record);

    int insertSelective(PmsCommentReply record);

    List<PmsCommentReply> selectByExample(PmsCommentReplyExample example);

    PmsCommentReply selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsCommentReply record, @Param("example") PmsCommentReplyExample example);

    int updateByExample(@Param("record") PmsCommentReply record, @Param("example") PmsCommentReplyExample example);

    int updateByPrimaryKeySelective(PmsCommentReply record);

    int updateByPrimaryKey(PmsCommentReply record);
}