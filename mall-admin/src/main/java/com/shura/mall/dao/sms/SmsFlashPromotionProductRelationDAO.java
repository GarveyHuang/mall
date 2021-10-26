package com.shura.mall.dao.sms;

import com.shura.mall.domain.sms.SmsFlashPromotionProductResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 限时购商品关联自定义 DAO
 */
public interface SmsFlashPromotionProductRelationDAO {

    /**
     * 获取限时购及相关商品信息
     */
    List<SmsFlashPromotionProductResult> getList(@Param("flashPromotionId") Long flashPromotionId, @Param("flashPromotionSessionId") Long flashPromotionSessionId);
}
