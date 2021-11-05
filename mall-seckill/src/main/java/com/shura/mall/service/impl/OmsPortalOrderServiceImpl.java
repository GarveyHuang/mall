package com.shura.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.api.ResultCode;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.common.enums.OrderStatus;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.component.CancelOrderSender;
import com.shura.mall.dao.PortalOrderDAO;
import com.shura.mall.dao.PortalOrderItemDAO;
import com.shura.mall.domain.*;
import com.shura.mall.feignapi.pms.PmsProductFeignApi;
import com.shura.mall.feignapi.sms.SmsCouponFeignApi;
import com.shura.mall.feignapi.ums.UmsMemberFeignApi;
import com.shura.mall.mapper.*;
import com.shura.mall.model.oms.*;
import com.shura.mall.model.sms.*;
import com.shura.mall.model.ums.UmsIntegrationConsumeSetting;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import com.shura.mall.service.OmsCartItemService;
import com.shura.mall.service.OmsPortalOrderService;
import com.shura.mall.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 前台订单管理 Service 实现类
 */
@Slf4j
@Service("portalOrderService")
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {


    @Autowired
    private OmsCartItemService cartItemService;

    @Autowired
    private UmsMemberFeignApi umsMemberFeignApi;

    @Autowired
    private SmsCouponFeignApi smsCouponFeignApi;

    @Autowired
    private PmsProductFeignApi pmsProductFeignApi;

    @Autowired
    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private OmsOrderSettingMapper orderSettingMapper;

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Autowired
    private PortalOrderItemDAO portalOrderItemDAO;

    @Autowired
    private PortalOrderDAO portalOrderDAO;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> itemIds, Long memberId) throws BusinessException {
        ConfirmOrderResult result = new ConfirmOrderResult();

        // 获取购物车信息
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listSelectedPromotion(memberId, itemIds);
        result.setCartPromotionItemList(cartPromotionItemList);

        // 获取用户收货地址列表
        List<UmsMemberReceiveAddress> memberReceiveAddressList = umsMemberFeignApi.list().getData();
        result.setMemberReceiveAddressList(memberReceiveAddressList);

        // 获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = smsCouponFeignApi.listCart(1, cartPromotionItemList).getData();
        result.setCouponHistoryDetailList(couponHistoryDetailList);

        // 获取用户信息 - 用户积分
        UmsMember member = umsMemberFeignApi.getMemberById().getData();
        result.setMemberIntegration(member.getIntegration());

        // 获取积分使用规则
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        result.setIntegrationConsumeSetting(integrationConsumeSetting);

        // 计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartPromotionItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    @Override
    public CommonResult generateOrder(OrderParam orderParam, Long memberId) throws BusinessException {
        List<OmsOrderItem> orderItemList = new ArrayList<>();

        UmsMember member = umsMemberFeignApi.getMemberById().getData();

        List<CartPromotionItem> cartPromotionItemList = cartItemService.listSelectedPromotion(memberId, orderParam.getItemIds());
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            // 生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartPromotionItem.getProductId());
            orderItem.setProductName(cartPromotionItem.getProductName());
            orderItem.setProductPic(cartPromotionItem.getProductPic());
            orderItem.setProductAttr(cartPromotionItem.getProductAttr());
            orderItem.setProductBrand(cartPromotionItem.getProductBrand());
            orderItem.setProductSn(cartPromotionItem.getProductSn());
            orderItem.setProductPrice(cartPromotionItem.getPrice());
            orderItem.setProductQuantity(cartPromotionItem.getQuantity());
            orderItem.setProductSkuId(cartPromotionItem.getProductSkuId());
            orderItem.setProductSkuCode(cartPromotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartPromotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(cartPromotionItem.getReduceAmount());
            orderItem.setPromotionName(cartPromotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(cartPromotionItem.getIntegration());
            orderItem.setGiftGrowth(cartPromotionItem.getGrowth());
            orderItemList.add(orderItem);
        }

        // 判断购物车中的商品是否都有库存
        if (!hasStock(cartPromotionItemList)) {
            return CommonResult.failed("库存不足，无法下单");
        }

        // 判断是否使用了优惠券
        if (orderParam.getCouponId() == null) {
            // 不使用优惠券
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setCouponAmount(new BigDecimal("0"));
            }
        } else {
            // 使用优惠券
            SmsCouponHistoryDetail couponHistoryDetail = getUseCoupon(cartPromotionItemList, orderParam.getCouponId());
            if (couponHistoryDetail == null) {
                return CommonResult.failed("该优惠券不可用");
            }

            // 对下单商品的优惠券进行处理
            handleCouponAmount(orderItemList, couponHistoryDetail);
        }

        // 判断是否使用积分
        if (orderParam.getUseIntegration() != null) {
            // 不使用积分
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setIntegrationAmount(new BigDecimal("0"));
            }
        } else {
            // 使用积分
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount,
                    member.getIntegration(), orderParam.getCouponId() != null);

            if (integrationAmount.compareTo(new BigDecimal("0")) == 0) {
                return CommonResult.failed("积分不可用");
            }

            // 可用情况下均摊到可用商品中
            for (OmsOrderItem orderItem : orderItemList) {
                BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
                orderItem.setIntegrationAmount(perAmount);
            }
        }

        // 计算 orderItem 的实付金额
        handleRealAmount(orderItemList);
        // 使用分布式事务进行库存锁定
        CommonResult lockResult = pmsProductFeignApi.lockStock(cartPromotionItemList);
        if (lockResult.getCode() == ResultCode.FAILED.getCode()) {
            // 扣减库存失败
            log.warn("远程调用商品服务锁定库存失败");
            throw new RuntimeException("远程调用商品服务锁定库存失败");
        }

        // 根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal("0"));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(new BigDecimal("0"));
        order.setPromotionAmount(calcPromotionAmount(orderItemList));
        order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
        if (orderParam.getCouponId() == null) {
            order.setCouponAmount(new BigDecimal("0"));
        } else {
            order.setCouponId(orderParam.getCouponId());
            order.setCouponAmount(calcCouponAmount(orderItemList));
        }

        if (orderParam.getUseIntegration() == null) {
            order.setUseIntegration(0);
            order.setIntegrationAmount(new BigDecimal("0"));
        } else {
            order.setUseIntegration(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }

        order.setPayAmount(calcPayAmount(order));

        // 转化为订单信息并插入数据库
        order.setMemberId(memberId);
        order.setCreateTime(new Date());
        order.setMemberUsername(member.getUsername());
        // 支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        // 订单来源：0->PC 订单；1->APP 订单
        order.setSourceType(1);
        // 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        // 订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);
        // 订单确认类型：0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);

        // 收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress receiveAddress = umsMemberFeignApi.getAddress(orderParam.getMemberReceiveAddressId()).getData();
        order.setReceiverName(receiveAddress.getName());
        order.setReceiverPhone(receiveAddress.getPhoneNumber());
        order.setReceiverPostCode(receiveAddress.getPostCode());
        order.setReceiverProvince(receiveAddress.getProvince());
        order.setReceiverCity(receiveAddress.getCity());
        order.setReceiverRegion(receiveAddress.getRegion());
        order.setReceiverDetailAddress(receiveAddress.getDetailAddress());

        // 计算赠送积分
        order.setIntegration(calcGiftIntegration(orderItemList));
        // 计算赠送成长值
        order.setGrowth(calcGifGrowth(orderItemList));
        // 生成订单号
        order.setOrderSn(generateOrderSn(order));

        orderMapper.insert(order);

        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        portalOrderItemDAO.insertList(orderItemList);

        // 如使用了优惠券，需要更新优惠券状态
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), memberId, 1);
        }

        // 如使用了积分，需要扣除积分
        if (orderParam.getUseIntegration() != null) {
            // TODO 这里需要做分布式事务
            member.setIntegration(member.getIntegration() - orderParam.getUseIntegration());
            CommonResult<String> result = umsMemberFeignApi.updateMember(member);
            if (result.getCode() == ResultCode.FAILED.getCode()) {
                log.warn("远程调用会员服务扣除用户积分异常");
                throw new RuntimeException("远程调用会员服务扣除用户积分异常");
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        return CommonResult.success(result, "下单成功");
    }

    @Override
    public CommonResult getDetailOrder(Long orderId) {
        return CommonResult.success(portalOrderDAO.getDetail(orderId));
    }

    @Override
    public Integer paySuccess(Long orderId, Integer payType) {
        // 修改订单状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(1);
        order.setPayType(payType);
        order.setPaymentTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        // 恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = portalOrderDAO.getDetail(orderId);
        return portalOrderDAO.updateSkuStock(orderDetail.getOrderItemList());
    }

    @Override
    public CommonResult cancelTimeoutOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        // 查询超时、未支付的订单及订单详情
        List<OmsOrderDetail> timeoutOrderList = portalOrderDAO.getTimeoutOrders(orderSetting.getNormalOrderOvertime());
        if (CollectionUtils.isEmpty(timeoutOrderList)) {
            return CommonResult.failed("暂无超时订单");
        }

        // 修改订单状态为交易取消
        List<Long> ids = timeoutOrderList.stream().map(OmsOrderDetail::getId).collect(Collectors.toList());
        portalOrderDAO.updateOrderStatus(ids, OrderStatus.CLOSED.getStatus());
        for (OmsOrderDetail timeoutOrder : timeoutOrderList) {
            if (CollectionUtils.isEmpty(timeoutOrder.getOrderItemList())) {
                throw new RuntimeException("订单商品不能为空");
            }

            // 删除订单商品库存锁定
            portalOrderDAO.releaseSkuStockLock(timeoutOrder.getOrderItemList());
            // 修改优惠券使用状态
            updateCouponStatus(timeoutOrder.getCouponId(), timeoutOrder.getMemberId(), 0);
            // 返还使用积分
            if (timeoutOrder.getUseIntegration() != null) {
                // TODO 这里需要做分布式事务
                UmsMember member = umsMemberFeignApi.getMemberById().getData();
                member.setIntegration(member.getIntegration() + timeoutOrder.getUseIntegration());
                CommonResult<String> result = umsMemberFeignApi.updateMember(member);
                if (result.getCode() == ResultCode.FAILED.getCode()) {
                    log.warn("远程调用会员服务返还用户积分异常");
                    throw new RuntimeException("远程调用会员服务返还用户积分异常");
                }
            }
        }
        return CommonResult.success(null);
    }

    @Override
    public void cancelOrder(Long orderId, Long memberId) {
        // 查询为付款的取消订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0).andDeleteStatusEqualTo(0);
        List<OmsOrder> cancelOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            return;
        }

        OmsOrder cancelOrder = cancelOrderList.get(0);
        if (cancelOrder != null) {
            // 修改订单状态为取消
            cancelOrder.setStatus(OrderStatus.CLOSED.getStatus());
            orderMapper.updateByPrimaryKeySelective(cancelOrder);
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            // 解除订单商品库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                portalOrderDAO.releaseSkuStockLock(orderItemList);
            }

            // 修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            // 返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                // TODO 这里需要做分布式事务
                UmsMember member = umsMemberFeignApi.getMemberById().getData();
                member.setIntegration(member.getIntegration() + cancelOrder.getUseIntegration());
                CommonResult<String> result = umsMemberFeignApi.updateMember(member);
                if (result.getCode() == ResultCode.FAILED.getCode()) {
                    log.warn("远程调用会员服务返还用户积分异常");
                    throw new RuntimeException("远程调用会员服务返还用户积分异常");
                }
            }
        }
    }

    @Override
    public int deleteOrder(Long orderId) {
        return 0;
    }

    @Override
    public void sendDelayMessageCancelOrder(MqCancelOrder mqCancelOrder) {
        // 获取订单超时时间
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = 5000L;
        // 发送延迟消息
        cancelOrderSender.sendMessage(mqCancelOrder, delayTimes);
    }

    @Override
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status) {
        PageHelper.startPage(pageNum, pageSize);
        return CommonResult.success(portalOrderDAO.findMemberOrderList(memberId, status));
    }

    /**
     * 判断下单商品是否都有库存
     */
    private boolean hasStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            if (cartPromotionItem.getRealStock() == null || cartPromotionItem.getRealStock() <= 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 计算购物车中商品的价格
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<CartPromotionItem> cartPromotionItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal("0"));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            totalAmount = totalAmount.add(cartPromotionItem.getPrice().multiply(new BigDecimal(cartPromotionItem.getQuantity())));

            if (cartPromotionItem.getReduceAmount() != null) {
                promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            }
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }

    /**
     * 计算总金额
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * 计算订单促销优惠金额
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            if (item.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
            }
        }
        return promotionAmount;
    }

    /**
     * 计算优惠券优惠金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            if (item.getPromotionAmount() != null) {
                couponAmount = couponAmount.add(item.getCouponAmount().multiply(new BigDecimal(item.getProductQuantity())));
            }
        }
        return couponAmount;
    }

    /**
     * 计算积分优惠金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            if (item.getPromotionAmount() != null) {
                integrationAmount = integrationAmount.add(item.getCouponAmount().multiply(new BigDecimal(item.getProductQuantity())));
            }
        }
        return integrationAmount;
    }

    /**
     * 获取可用积分抵扣优惠金额
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, Integer memberIntegration, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal("0");
        // 判断用户是否有足够的积分
        if (useIntegration.compareTo(memberIntegration) > 0) {
            return zeroAmount;
        }

        // 根据积分使用规则判断是否可用
        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);
        // 是否可以与优惠券共用
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            // 不可与优惠券共用
            return zeroAmount;
        }

        // 是否达到最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }

        // 是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration)
                .divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);

        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(totalAmount.multiply(maxPercent)) > 0) {
            // 超过订单抵用最高百分比
            return zeroAmount;
        }

        return integrationAmount;
    }

    /**
     * 计算应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        // 应付金额 = 总金额 + 运费 - 促销优惠 - 优惠券优惠 - 积分抵扣
        return order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
    }

    /**
     * 计算订单赠送的积分
     */
    private Integer calcGiftIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem item : orderItemList) {
            sum += item.getGiftIntegration() * item.getProductQuantity();
        }

        return sum;
    }

    /**
     * 计算订单赠送的成长值
     */
    private Integer calcGifGrowth(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem item : orderItemList) {
            sum += item.getGiftGrowth() * item.getProductQuantity();
        }

        return sum;
    }

    /**
     * 对优惠券优惠进行处理
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (Objects.equals(coupon.getUseType(), 0)) {
            // 全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (Objects.equals(coupon.getUseType(), 1)) {
            // 指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (Objects.equals(coupon.getUseType(), 2)) {
            // 指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对实际金额进行处理
     */
    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem item : orderItemList) {
            // 原价 - 促销优惠 - 优惠券抵扣 - 积分抵扣
            BigDecimal realAmount = item.getProductPrice();

            if (item.getPromotionAmount() != null) {
                realAmount = realAmount.subtract(item.getPromotionAmount());
            }

            if (item.getCouponAmount() != null) {
                realAmount = realAmount.subtract(item.getCouponAmount());
            }

            if (item.getIntegrationAmount() != null) {
                realAmount = realAmount.subtract(item.getIntegrationAmount());
            }

            item.setRealAmount(realAmount);
        }
    }

    /**
     * 获取订单促销信息
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem item : orderItemList) {
            sb.append(item.getPromotionName()).append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * 对每个下单商品进行优惠券金额分摊计算
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem item : orderItemList) {
            // (商品价格 / 可用商品总价) * 优惠券面额
            BigDecimal couponAmount = item.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN)
                    .multiply(coupon.getAmount());
            item.setCouponAmount(couponAmount);
        }
    }

    /**
     * 获取与优惠券有关的下单商品
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList       下单商品
     * @param type                使用关系类型：0->指定分类；1->指定商品
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = couponHistoryDetail.getCategoryRelationList()
                    .stream().map(SmsCouponProductCategoryRelation::getProductCategoryId).collect(Collectors.toList());

            for (OmsOrderItem item : orderItemList) {
                if (categoryIdList.contains(item.getProductCategoryId())) {
                    result.add(item);
                } else {
                    item.setCouponAmount(new BigDecimal("0"));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = couponHistoryDetail.getProductRelationList()
                    .stream().map(SmsCouponProductRelation::getProductId).collect(Collectors.toList());

            for (OmsOrderItem item : orderItemList) {
                if (productIdList.contains(item.getProductId())) {
                    result.add(item);
                } else {
                    item.setCouponAmount(new BigDecimal("0"));
                }
            }
        }

        return result;
    }

    /**
     * 获取用户可使用的优惠券
     */
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        List<SmsCouponHistoryDetail> couponHistoryDetailList = smsCouponFeignApi.listCart(1, cartPromotionItemList).getData();

        return couponHistoryDetailList.stream()
                .filter(couponHistoryDetail -> Objects.equals(couponHistoryDetail.getCouponId(), couponId))
                .findFirst().orElse(null);
    }

    /**
     * 将优惠券信息更新为指定状态
     * @param couponId  优惠券 id
     * @param memberId  会员 id
     * @param useStatus 使用状态：0->未使用；1->已使用
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) {
            return;
        }

        // 查询优惠券信息
        SmsCouponHistoryExample example = new SmsCouponHistoryExample();
        example.createCriteria()
                .andMemberIdEqualTo(memberId)
                .andCouponIdEqualTo(couponId)
                .andUseStatusEqualTo(useStatus == 0 ? 1 : 0);
        List<SmsCouponHistory> couponHistoryList = couponHistoryMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(couponHistoryList)) {
            SmsCouponHistory couponHistory = couponHistoryList.get(0);
            couponHistory.setUseTime(new Date());
            couponHistory.setUseStatus(useStatus);
            couponHistoryMapper.updateByPrimaryKeySelective(couponHistory);
        }
    }

    /**
     * 生成 18 位订单编号：8 位日期 + 2 位平台号码 + 2 位支付方式 + 6 位以上自增 id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = RedisKeyPrefixConst.ORDER_ID_CACHE_PREFIX + date;
        Long increment = redisOpsUtil.increment(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }
}
