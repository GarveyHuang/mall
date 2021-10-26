package com.shura.mall.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.mapper.SmsCouponHistoryMapper;
import com.shura.mall.model.sms.SmsCouponHistory;
import com.shura.mall.model.sms.SmsCouponHistoryExample;
import com.shura.mall.service.sms.SmsCouponHistoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 优惠券领取记录管理 Service
 */
@Service("couponHistoryService")
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {

    @Autowired
    private SmsCouponHistoryMapper historyMapper;

    @Override
    public List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);

        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        SmsCouponHistoryExample.Criteria criteria = example.createCriteria();

        if (couponId != null) {
            criteria.andCouponIdEqualTo(couponId);
        }

        if (useStatus != null) {
            criteria.andUseStatusEqualTo(useStatus);
        }

        if (StringUtils.isNotBlank(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }

        return historyMapper.selectByExample(example);
    }
}
