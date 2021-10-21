package com.shura.mall.domain;

import com.shura.mall.model.cms.CmsSubject;
import com.shura.mall.model.pms.PmsBrand;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.sms.SmsHomeAdvertise;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 首页内容返回信息封装
 */
@Getter
@Setter
public class HomeContentResult {

    // 轮播广告
    private List<SmsHomeAdvertise> advertiseList;
    // 推荐品牌
    private List<PmsBrand> brandList;

    // 促销商品
    private List<FlashPromotionProduct> homeFlashPromotion;

    // 新品推荐
    private List<PmsProduct> newProductList;

    // 人气推荐
    private List<PmsProduct> hotProductList;

    // 推荐专题
    private List<CmsSubject> subjectList;
}
