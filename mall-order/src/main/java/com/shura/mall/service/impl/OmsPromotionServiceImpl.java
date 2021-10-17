package com.shura.mall.service.impl;

import com.shura.mall.service.IOmsPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 促销管理 Service 实现类
 */
@Service("promotionService")
public class OmsPromotionServiceImpl implements IOmsPromotionService {

    @Autowired
    private PmsPproductFeignApi pmsPproductFeignApi;
}
