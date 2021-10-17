package com.shura.mall.service.impl;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.dao.PortalOrderItemDAO;
import com.shura.mall.domain.ConfirmOrderResult;
import com.shura.mall.domain.MqCancelOrder;
import com.shura.mall.domain.OmsOrderDetail;
import com.shura.mall.domain.OrderParam;
import com.shura.mall.feignapi.pms.PmsProductFeignApi;
import com.shura.mall.feignapi.ums.UmsCouponFeignApi;
import com.shura.mall.feignapi.ums.UmsMemberFeignApi;
import com.shura.mall.mapper.*;
import com.shura.mall.service.IOmsCartItemService;
import com.shura.mall.service.IOmsPortalOrderService;
import com.shura.mall.util.RedisOpsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 订单管理 Service 实现类
 */
@Service("portalOrderService")
public class OmsPortalOrderServiceImpl implements IOmsPortalOrderService {

    @Value("#{redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;

    @Autowired
    private IOmsCartItemService cartItemService;

    @Autowired
    private UmsMemberFeignApi memberFeignApi;

    @Autowired
    private UmsCouponFeignApi couponFeignApi;

    @Autowired
    private PmsProductFeignApi productFeignApi;

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Autowired
    private PortalOrderItemDAO portalOrderItemDAO;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> itemIds, Long memberId) throws BusinessException {
        return null;
    }

    @Override
    public CommonResult generateOrder(OrderParam orderParam, Long memberId) throws BusinessException {
        return null;
    }

    @Override
    public CommonResult getDetailOrder(Long orderId) {
        return null;
    }

    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        return null;
    }

    @Override
    public CommonResult cancelTimeOutOrder() {
        return null;
    }

    @Override
    public void cancelOrder(Long orderId, Long memberId) {

    }

    @Override
    public int deleteOrder(Long orderId) {
        return 0;
    }

    @Override
    public void sendDelayMessageCancelOrder(MqCancelOrder mqCancelOrder) {

    }

    @Override
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status) {
        return null;
    }
}
