package com.shura.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.api.ResultCode;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.dao.PortalOrderDAO;
import com.shura.mall.dao.PortalOrderItemDAO;
import com.shura.mall.domain.*;
import com.shura.mall.feignapi.pms.PmsProductFeignApi;
import com.shura.mall.feignapi.ums.UmsCouponFeignApi;
import com.shura.mall.feignapi.ums.UmsMemberFeignApi;
import com.shura.mall.mapper.*;
import com.shura.mall.model.oms.*;
import com.shura.mall.model.pms.PmsSkuStock;
import com.shura.mall.model.sms.*;
import com.shura.mall.model.ums.UmsIntegrationConsumeSetting;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import com.shura.mall.service.IOmsCartItemService;
import com.shura.mall.service.IOmsPortalOrderService;
import com.shura.mall.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 订单管理 Service 实现类
 */
@Slf4j
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
    private PortalOrderDAO portalOrderDAO;

    @Autowired
    private PortalOrderItemDAO portalOrderItemDAO;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public ConfirmOrderResult generateConfirmOrder(List<Long> itemIds, Long memberId) throws BusinessException {
        ConfirmOrderResult result = new ConfirmOrderResult();

        // 获取购物车信息
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listSelectedPromotion(memberId, itemIds);
        result.setCartPromotionItemList(cartPromotionItemList);

        // 获取用户收货地址列表
        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberFeignApi.list().getData();
        result.setMemberReceiveAddressList(memberReceiveAddressList);

        // 获取用户可用优惠券列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = couponFeignApi.listCart(1, cartPromotionItemList).getData();
        result.setCouponHistoryDetailList(couponHistoryDetailList);

        // 获取用户积分
        UmsMember member = memberFeignApi.getMemberById().getData();
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

        // 获取用户信息
        UmsMember member = memberFeignApi.getMemberById().getData();

        // 选择购物车商品下单
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listSelectedPromotion(memberId, orderParam.getItemIds());
        for (CartPromotionItem promotionItem : cartPromotionItemList) {
            // 生成下单商品信息
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(promotionItem.getProductId());
            orderItem.setProductName(promotionItem.getProductName());
            orderItem.setProductPic(promotionItem.getProductPic());
            orderItem.setProductAttr(promotionItem.getProductAttr());
            orderItem.setProductBrand(promotionItem.getProductBrand());
            orderItem.setProductSn(promotionItem.getProductSn());
            orderItem.setProductPrice(promotionItem.getPrice());
            orderItem.setProductQuantity(promotionItem.getQuantity());
            orderItem.setProductSkuId(promotionItem.getProductSkuId());
            orderItem.setProductSkuCode(promotionItem.getProductSkuCode());
            orderItem.setProductCategoryId(promotionItem.getProductCategoryId());
            orderItem.setPromotionAmount(promotionItem.getReduceAmount());
            orderItem.setPromotionName(promotionItem.getPromotionMessage());
            orderItem.setGiftIntegration(promotionItem.getIntegration());
            orderItem.setGiftGrowth(promotionItem.getGrowth());
            orderItemList.add(orderItem);
        }

        // 判断购物车中商品是否都有库存
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

        // 判断是否使用了积分
        if (orderParam.getUseIntegration() == null) {
            // 不使用积分
            for (OmsOrderItem orderItem : orderItemList) {
                orderItem.setIntegrationAmount(new BigDecimal("0"));
            }
        } else {
            // 使用积分
            BigDecimal totalAmount = calcTotalAmount(orderItemList);
            BigDecimal integrationAmount = getUseIntegrationAmount(orderParam.getUseIntegration(), totalAmount, member.getIntegration(), orderParam.getCouponId() != null);
            if (integrationAmount.compareTo(new BigDecimal("0")) == 0) {
                return CommonResult.failed("积分不可用");
            } else {
                // 积分可用情况下分摊到可用商品中
                for (OmsOrderItem orderItem : orderItemList) {
                    BigDecimal perAmount = orderItem.getProductPrice().divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(integrationAmount);
                    orderItem.setIntegrationAmount(perAmount);
                }
            }
        }

        // 计算 orderItem 的实付金额
        handleRealAmount(orderItemList);
        // 进行库存锁定，这里使用到了分布式事务
        CommonResult lockResult = productFeignApi.lockStock(cartPromotionItemList);
        if (lockResult.getCode() == ResultCode.FAILED.getCode()) {
            // 标识扣件库存失败
            log.warn("远程调用锁定库存失败！");
            throw new RuntimeException("远程调用锁定库存失败");
        }

        // 根据商品合集、运费、活动优惠、优惠券、积分计算应付金额
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
            order.setIntegration(0);
            order.setIntegrationAmount(new BigDecimal("0"));
        } else {
            order.setIntegration(orderParam.getUseIntegration());
            order.setIntegrationAmount(calcIntegrationAmount(orderItemList));
        }
        order.setPayAmount(calcPayAmount(order));
        order.setMemberId(memberId);
        order.setMemberUsername(member.getUsername());
        order.setCreateTime(new Date());
        // 支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        // 订单来源：0->PC；1->app 订单
        order.setSourceType(1);
        // 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        // 订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);

        // 获取用户收货地址
        UmsMemberReceiveAddress receiveAddress = memberFeignApi.getItem(orderParam.getMemberReceiveAddressId()).getData();

        order.setReceiverName(receiveAddress.getName());
        order.setReceiverPhone(receiveAddress.getPhoneNumber());
        order.setReceiverPostCode(receiveAddress.getPostCode());
        order.setReceiverProvince(receiveAddress.getProvince());
        order.setReceiverCity(receiveAddress.getCity());
        order.setReceiverRegion(receiveAddress.getRegion());
        order.setReceiverDetailAddress(receiveAddress.getDetailAddress());
        // 订单确认状态：0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        // 计算赠送积分
        order.setIntegration(calcGifIntegration(orderItemList));
        // 计算赠送成长值
        order.setGrowth(calcGifGrowth(orderItemList));
        // 生成订单号
        order.setOrderSn(generateOrderSn(order));
        // 插入 order 表和 order_item 表
        orderMapper.insert(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            order.setOrderSn(order.getOrderSn());
        }
        portalOrderItemDAO.insertList(orderItemList);
        // 如使用优惠券，要更新优惠券使用状态
        if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), memberId, 1);
        }

        // 如使用积分，需要扣减积分
        if (orderParam.getUseIntegration() != null) {
            order.setUseIntegration(orderParam.getUseIntegration());
            // 远程调用会员服务扣减积分
            // TODO 这里需要做分布式事务
            member.setIntegration(member.getIntegration() - orderParam.getUseIntegration());
            CommonResult<String> result = memberFeignApi.updateUmsMember(member);
            if (result.getCode() == ResultCode.FAILED.getCode()) {
                log.warn("远程调用会员服务扣减用户积分异常");
                throw new RuntimeException("远程调用会员服务扣减用户积分异常");
            }
        }

        // 删除购物车中的下单商品
//        deleteCartTimeList(cartPromotionItemList, memberId);
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
        // 修改订单支付状态
        OmsOrder order = new OmsOrder();
        order.setId(orderId);
        order.setStatus(1);
        order.setPaymentTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        // 恢复所有下单商品的锁定库存，扣减真实库存
        OmsOrderDetail orderDetail = portalOrderDAO.getDetail(orderId);
        return portalOrderDAO.updateSkuStock(orderDetail.getOrderItemList());
    }

    @Override
    public CommonResult cancelTimeOutOrder() {
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        // 查询超时、未支付的订单及订单详情
        List<OmsOrderDetail> timeOutOrders = portalOrderDAO.getTimeOutOrders(orderSetting.getNormalOrderOvertime());
        if (CollectionUtils.isEmpty(timeOutOrders)) {
            return CommonResult.failed("暂无超时订单");
        }
        // 修改订单状态为交易取消
        List<Long> ids = new ArrayList<>();
        for (OmsOrderDetail timeoutOrder : timeOutOrders) {
            ids.add(timeoutOrder.getId());
        }
        portalOrderDAO.updateOrderStatus(ids, 4);

        for (OmsOrderDetail timeoutOrder : timeOutOrders) {
            if (CollectionUtils.isEmpty(timeoutOrder.getOrderItemList())) {
                throw new RuntimeException("订单商品不存在");
            }

            // 解除订单商品库存锁定
            portalOrderDAO.releaseSkuStockLock(timeoutOrder.getOrderItemList());

            // 修改优惠券使用状态
            updateCouponStatus(timeoutOrder.getCouponId(), timeoutOrder.getMemberId(), 0);

            // 返还使用积分
            if (timeoutOrder.getUseIntegration() != null) {
                // TODO 这里需要做分布式事务
                UmsMember member = memberFeignApi.getMemberById().getData();
                member.setIntegration(member.getIntegration() + timeoutOrder.getUseIntegration());
                CommonResult<String> result = memberFeignApi.updateUmsMember(member);
                if (result.getCode() == ResultCode.FAILED.getCode()) {
                    log.warn("远程调用会员服务扣除用户积分异常");
                    throw new RuntimeException("远程调用会员服务扣除用户积分异常");
                }
            }
        }
        return CommonResult.success(null);
    }

    @Override
    public void cancelOrder(Long orderId, Long memberId) {
        // 查询未付款的取消订单
        OmsOrderExample example = new OmsOrderExample();
        example.createCriteria().andIdEqualTo(orderId).andStatusEqualTo(0).andDeleteStatusEqualTo(0);
        List<OmsOrder> cancelOrderList = orderMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(cancelOrderList)) {
            return;
        }

        OmsOrder cancelOrder = cancelOrderList.get(0);
        if (cancelOrder != null) {
            // 修改订单状态为取消
            cancelOrder.setStatus(4);
            orderMapper.updateByPrimaryKeySelective(cancelOrder);

            OmsOrderItemExample itemExample = new OmsOrderItemExample();
            itemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(itemExample);
            // 解除订单商品的库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                portalOrderDAO.releaseSkuStockLock(orderItemList);
            }
            // 修改优惠券使用状态
            updateCouponStatus(cancelOrder.getCouponId(), cancelOrder.getMemberId(), 0);
            // 返还使用积分
            if (cancelOrder.getUseIntegration() != null) {
                // TODO 这里要做分布式事务
                UmsMember member = memberFeignApi.getMemberById().getData();
                member.setIntegration(member.getIntegration() + cancelOrder.getUseIntegration());
                CommonResult<String> result = memberFeignApi.updateUmsMember(member);
                if (result.getCode() == ResultCode.FAILED.getCode()) {
                    log.warn("远程调用会员服务扣除用户积分异常");
                    throw new RuntimeException("远程调用会员服务扣除用户积分异常");
                }
            }
        }
    }

    @Override
    public int deleteOrder(Long orderId) {
        return portalOrderDAO.deleteOrder(orderId);
    }

    @Override
    public void sendDelayMessageCancelOrder(MqCancelOrder mqCancelOrder) {
        // 获取订单超时时间
        OmsOrderSetting orderSetting = orderSettingMapper.selectByPrimaryKey(1L);
        long delayTimes = 5000L;
        // 发送延迟消息
        // TODO 这里还没引入 MQ
//        cancelOrderSender.sendMessage(mqCancelOrder, delayTimes);
    }

    @Override
    public CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status) {
        PageHelper.startPage(pageNum, pageSize);

        return CommonResult.success(portalOrderDAO.findMemberOrderList(memberId, status));
    }

    /**
     * 生成 18 位订单编号：8 位日期 + 2 位平台号码 + 2 位支付方式 + 6 位以上自增 id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_KEY_PREFIX_ORDER_ID + date;
        Long increment = redisOpsUtil.increment(key, + 1);
        sb.append(date)
                .append(String.format("%02d", order.getSourceType()))
                .append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", incrementStr));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    /**
     * 删除下单商品的购物车信息
     */
    private void deleteCartItemList(List<CartPromotionItem> cartPromotionItemList, Long memberId) {
        List<Long> ids = new ArrayList<>();
        for (CartPromotionItem promotionItem : cartPromotionItemList) {
            ids.add(promotionItem.getId());
        }
        cartItemService.delete(memberId, ids);
    }

    /**
     * 计算订单赠送的成长值
     */
    private Integer calcGifGrowth(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum = sum + orderItem.getGiftGrowth() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     * 计算订单赠送的积分
     */
    private Integer calcGifIntegration(List<OmsOrderItem> orderItemList) {
        int sum = 0;
        for (OmsOrderItem orderItem : orderItemList) {
            sum = sum + orderItem.getGiftIntegration() * orderItem.getProductQuantity();
        }
        return sum;
    }

    /**
     *
     * @param couponId
     * @param memberId
     * @param useStatus
     */
    private void updateCouponStatus(Long couponId, Long memberId, Integer useStatus) {
        if (couponId == null) {
            return;
        }

        // 查询第一张优惠券
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
     * 处理商品的真实金额
     * @param orderItemList
     */
    private void handleRealAmount(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem orderItem : orderItemList) {
            // 真实价格 = 原价 - 促销价 - 优惠券折扣 - 积分扣减
            BigDecimal realAmount = orderItem.getProductPrice();
            if (null != orderItem.getPromotionAmount()) {
                realAmount.subtract(orderItem.getPromotionAmount());
            }

            if (null != orderItem.getCouponAmount()) {
                realAmount.subtract(orderItem.getCouponAmount());
            }

            if (null != orderItem.getIntegrationAmount()) {
                realAmount.subtract(orderItem.getIntegrationAmount());
            }
            orderItem.setRealAmount(realAmount);
        }
    }

    /**
     * 计算订单积分金额
     */
    private BigDecimal calcIntegrationAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal integrationAmount = new BigDecimal("0");
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getIntegrationAmount() != null) {
                integrationAmount = integrationAmount.add(orderItem.getIntegrationAmount())
                        .multiply(new BigDecimal(orderItem.getProductQuantity()));
            }
        }
        return integrationAmount;
    }

    /**
     * 计算订单优惠券金额
     */
    private BigDecimal calcCouponAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal couponAmount = new BigDecimal("0");
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getCouponAmount() != null) {
                couponAmount = couponAmount.add(orderItem.getCouponAmount())
                        .multiply(new BigDecimal(orderItem.getProductQuantity()));
            }
        }
        return couponAmount;
    }

    /**
     * 计算活动优惠
     */
    private BigDecimal calcPromotionAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal promotionAmount = new BigDecimal("0");
        for (OmsOrderItem orderItem : orderItemList) {
            if (orderItem.getPromotionAmount() != null) {
                promotionAmount = promotionAmount.add(orderItem.getCouponAmount())
                        .multiply(new BigDecimal(orderItem.getProductQuantity()));
            }
        }
        return promotionAmount;
    }

    /**
     * 计算订单应付金额
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        // 应付 = 原价 + 运费 - 促销价 - 优惠券折扣 - 积分扣减
        return order.getTotalAmount()
                .add(order.getFreightAmount())
                .subtract(order.getPromotionAmount())
                .subtract(order.getCouponAmount())
                .subtract(order.getIntegrationAmount());
    }

    /**
     * 获取可用积分抵扣金额
     * @param useIntegration    使用的积分数量
     * @param totalAmount       订单总金额
     * @param memberIntegration 用户的积分数量
     * @param hasCoupon         是否已使用优惠券
     * @return
     */
    private BigDecimal getUseIntegrationAmount(Integer useIntegration, BigDecimal totalAmount, Integer memberIntegration, boolean hasCoupon) {
        BigDecimal zeroAmount = new BigDecimal("0");
        // 判断用户是否有足够的积分
        if (useIntegration.compareTo(memberIntegration) > 0) {
            return zeroAmount;
        }

        UmsIntegrationConsumeSetting integrationConsumeSetting = integrationConsumeSettingMapper.selectByPrimaryKey(1L);

        // 根据积分规则判断是否可用，是否可与优惠券共用
        if (hasCoupon && integrationConsumeSetting.getCouponStatus().equals(0)) {
            // 不可与优惠券共用
            return zeroAmount;
        }

        // 是否达到了最低使用积分门槛
        if (useIntegration.compareTo(integrationConsumeSetting.getUseUnit()) < 0) {
            return zeroAmount;
        }

        // 是否超过订单抵用最高百分比
        BigDecimal integrationAmount = new BigDecimal(useIntegration)
                .divide(new BigDecimal(integrationConsumeSetting.getUseUnit()), 2, RoundingMode.HALF_EVEN);
        BigDecimal maxPercent = new BigDecimal(integrationConsumeSetting.getMaxPercentPerOrder())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
        if (integrationAmount.compareTo(maxPercent) > 0) {
            return zeroAmount;
        }
        return integrationAmount;
    }

    /**
     * 获取订单促销信息
     * @return
     */
    private String getOrderPromotionInfo(List<OmsOrderItem> orderItemList) {
        StringBuilder sb = new StringBuilder();
        for (OmsOrderItem orderItem : orderItemList) {
            sb.append(orderItem.getProductName()).append(",");
        }
        String result = sb.toString();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    /**
     * 对优惠券进行处理
     * @param orderItemList       order_time 列表
     * @param couponHistoryDetail 可用优惠券详情
     */
    private void handleCouponAmount(List<OmsOrderItem> orderItemList, SmsCouponHistoryDetail couponHistoryDetail) {
        SmsCoupon coupon = couponHistoryDetail.getCoupon();
        if (coupon.getUseType().equals(0)) {
            // 全场通用
            calcPerCouponAmount(orderItemList, coupon);
        } else if (coupon.getType().equals(1)) {
            // 指定分类
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 0);
            calcPerCouponAmount(couponOrderItemList, coupon);
        } else if (coupon.getUseType().equals(2)) {
            // 指定商品
            List<OmsOrderItem> couponOrderItemList = getCouponOrderItemByRelation(couponHistoryDetail, orderItemList, 1);
            calcPerCouponAmount(couponOrderItemList, coupon);
        }
    }

    /**
     * 对每个下单商品进行优惠券金额分摊的计算
     * @param orderItemList 可用优惠券的下单商品列表
     * @param coupon
     */
    private void calcPerCouponAmount(List<OmsOrderItem> orderItemList, SmsCoupon coupon) {
        BigDecimal totalAmount = calcTotalAmount(orderItemList);
        for (OmsOrderItem orderItem : orderItemList) {
            // 优惠券折扣价 = （商品价格 / 可用商品总价）* 优惠券面额
            BigDecimal couponAmount = orderItem.getProductPrice()
                    .divide(totalAmount, 3, RoundingMode.HALF_EVEN).multiply(coupon.getAmount());
            orderItem.setCouponAmount(couponAmount);
        }
    }

    /**
     * 获取与优惠券有关系的下单商品
     * @param couponHistoryDetail 优惠券详情
     * @param orderItemList       下单商品
     * @param type                使用关系类型：0->指定分类；1->指定商品
     * @return
     */
    private List<OmsOrderItem> getCouponOrderItemByRelation(SmsCouponHistoryDetail couponHistoryDetail, List<OmsOrderItem> orderItemList, int type) {
        List<OmsOrderItem> result = new ArrayList<>();
        if (type == 0) {
            List<Long> categoryIdList = new ArrayList<>();
            for (SmsCouponProductCategoryRelation productCategoryRelation : couponHistoryDetail.getCategoryRelationList()) {
                categoryIdList.add(productCategoryRelation.getProductCategoryId());
            }

            for (OmsOrderItem orderItem : orderItemList) {
                if (categoryIdList.contains(orderItem.getProductCategoryId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal("0"));
                }
            }
        } else if (type == 1) {
            List<Long> productIdList = new ArrayList<>();
            for (SmsCouponProductRelation couponProductRelation : couponHistoryDetail.getProductRelationList()) {
                productIdList.add(couponProductRelation.getProductId());
            }

            for (OmsOrderItem orderItem : orderItemList) {
                if (productIdList.contains(orderItem.getProductId())) {
                    result.add(orderItem);
                } else {
                    orderItem.setCouponAmount(new BigDecimal("0"));
                }
            }
        }

        return result;
    }

    /**
     * 获取该用户可以使用的优惠券
     *
     * @param cartPromotionItemList 购物车优惠列表
     * @param couponId              使用优惠券 id
     */
    private SmsCouponHistoryDetail getUseCoupon(List<CartPromotionItem> cartPromotionItemList, Long couponId) {
        // TODO 远程调用可用优惠卷列表
        List<SmsCouponHistoryDetail> couponHistoryDetailList = new ArrayList<>();
//        List<SmsCouponHistoryDetail> couponHistoryDetailList = memberFeignApi.listCart(cartPromotionItemList, 1);
        for (SmsCouponHistoryDetail couponHistoryDetail : couponHistoryDetailList) {
            if (couponHistoryDetail.getCoupon().getId().equals(couponId)) {
                return couponHistoryDetail;
            }
        }
        return null;
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
     * 锁定下单商品的所有库存
     */
    private void lockStock(List<CartPromotionItem> cartPromotionItemList) {
        for (CartPromotionItem cartPromotionItem : cartPromotionItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartPromotionItem.getProductSkuId());
            skuStock.setLockStock(skuStock.getLockStock() + cartPromotionItem.getQuantity());
            skuStockMapper.updateByPrimaryKeySelective(skuStock);
        }
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
            if (null!=cartPromotionItem.getReduceAmount()) {
                promotionAmount = promotionAmount.add(cartPromotionItem.getReduceAmount().multiply(new BigDecimal(cartPromotionItem.getQuantity())));
            }
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }
}