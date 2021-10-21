package com.shura.mall.dao;

import com.shura.mall.domain.PmsCommentParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description:
 */
public interface PortalProductCommentDAO {

    List<PmsCommentParam> getCommentList(Long productId);

    Integer selectUserOrder(@Param("userId") Long userId, @Param("productId") Long productId);
}
