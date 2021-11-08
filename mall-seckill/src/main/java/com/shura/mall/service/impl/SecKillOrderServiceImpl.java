package com.shura.mall.service.impl;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.common.enums.*;
import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.component.LocalCache;
import com.shura.mall.component.OrderMessageSender;
import com.shura.mall.dao.SecKillStockDAO;
import com.shura.mall.domain.*;
import com.shura.mall.feignapi.pms.PmsProductFeignApi;
import com.shura.mall.feignapi.ums.UmsMemberFeignApi;
import com.shura.mall.mapper.OmsOrderItemMapper;
import com.shura.mall.mapper.OmsOrderMapper;
import com.shura.mall.model.oms.OmsOrder;
import com.shura.mall.model.oms.OmsOrderItem;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import com.shura.mall.service.SecKillOrderService;
import com.shura.mall.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 秒杀订单管理 Service 实现类
 */
@Slf4j
@Service("secKillOrderService")
public class SecKillOrderServiceImpl implements SecKillOrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private SecKillStockDAO secKillStockDAO;

    @Autowired
    private PmsProductFeignApi pmsProductFeignApi;

    @Autowired
    private UmsMemberFeignApi umsMemberFeignApi;

    @Autowired
    private LocalCache<Boolean> cache;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Override
    public CommonResult generateConfirmSecKillOrder(Long productId, Long memberId, String token) throws BusinessException {
        // 1. 进行订单金额确认前的库存与购买权限检查
        CommonResult commonResult = confirmCheck(productId, memberId, token);
        if (commonResult.getCode() == 500) {
            return commonResult;
        }

        // 2. 从商品服务获取商品信息
        PmsProductParam product = getProductInfo(productId);
        if (product == null) {
            return CommonResult.failed("无效的商品！");
        }

        // 3. 验证秒杀时间是否超时
        if (!validateSecKillTime(product)) {
            return CommonResult.failed("秒杀活动未开始或已结束！");
        }

        // 4. 调用会员服务获取会员信息
        UmsMember member = umsMemberFeignApi.getMemberById().getData();

        ConfirmOrderResult result = new ConfirmOrderResult();

        // 5. 获取用户收货地址
        List<UmsMemberReceiveAddress> receiveAddressList = umsMemberFeignApi.list().getData();
        result.setMemberReceiveAddressList(receiveAddressList);

        // 6. 构建商品信息
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        CartPromotionItem promotionItem = new CartPromotionItem();
        promotionItem.setProductId(product.getId());
        promotionItem.setProductName(product.getName());
        promotionItem.setMemberId(memberId);
        promotionItem.setMemberNickname(member.getNickname());
        promotionItem.setProductPic(product.getPic());
        promotionItem.setProductBrand(product.getBrandName());
        promotionItem.setQuantity(1); // 购买数量，一次只能秒杀一件商品
        // 获取商品库存
        Integer stock = redisOpsUtil.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId, Integer.class);
        promotionItem.setRealStock(stock);
        promotionItem.setProductCategoryId(product.getProductCategoryId());
        promotionItem.setGrowth(product.getGiftGrowth());
        promotionItem.setIntegration(product.getGiftPoint());
        // 计算秒杀优惠价
        promotionItem.setReduceAmount(product.getPromotionPrice().subtract(product.getFlashPromotionPrice()));
        promotionItem.setPromotionMessage("秒杀特惠活动");
        cartPromotionItemList.add(promotionItem);
        result.setCartPromotionItemList(cartPromotionItemList);

        // 7. 计算订单总金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(product);
        result.setCalcAmount(calcAmount);

        // 8. 会员积分
        result.setMemberIntegration(member.getIntegration());
        return CommonResult.success(result);
    }

    @Override
    public CommonResult generateSecKillOrder(OrderParam orderParam, Long memberId, String token) throws BusinessException {
        Long productId = orderParam.getItemIds().get(0);
        // 1. 进行创建订单前的库存与购买权限检查
        CommonResult commonResult = confirmCheck(productId, memberId, token);
        if (commonResult.getCode() == 500) {
            return commonResult;
        }

        // 2. 从商品服务获取商品信息
        PmsProductParam product = getProductInfo(productId);

        // 3. 验证秒杀时间是否超时
        if (!validateSecKillTime(product)) {
            return CommonResult.failed("秒杀活动未开始或已结束！");
        }

        // 4. 调用会员服务获取会员信息
        UmsMember member = umsMemberFeignApi.getMemberById().getData();

        // 5. 调用会员服务获取会员收货地址
        UmsMemberReceiveAddress address = umsMemberFeignApi.getAddress(orderParam.getMemberReceiveAddressId()).getData();

        // 6. 预减库存
        if (!preDecrRedisStock(productId, product.getFlashPromotionRelationId())) {
            return CommonResult.failed("下单失败，已经抢购完啦～");
        }

        // 7. 生成下单商品信息
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductPic(product.getPic());
        orderItem.setProductBrand(product.getBrandName());
        orderItem.setProductSn(product.getProductSn());
        orderItem.setProductPrice(product.getFlashPromotionPrice());
        orderItem.setProductQuantity(1);
        orderItem.setProductCategoryId(product.getProductCategoryId());
        orderItem.setPromotionAmount(product.getPrice().subtract(product.getFlashPromotionPrice()));
        orderItem.setPromotionName("秒杀特惠活动");
        orderItem.setGiftIntegration(product.getGiftPoint());
        orderItem.setGiftGrowth(product.getGiftGrowth());
        orderItem.setCouponAmount(new BigDecimal("0"));
        orderItem.setIntegrationAmount(new BigDecimal("0"));

        // 支付金额 = 秒杀优惠价 * 数量
        BigDecimal payAmount = product.getFlashPromotionPrice().multiply(new BigDecimal("1"));
        // 优惠价格
        orderItem.setRealAmount(payAmount);

        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(product.getPrice().subtract(product.getFlashPromotionPrice()));
        order.setFreightAmount(new BigDecimal("0"));
        order.setPromotionAmount(new BigDecimal("0"));
        order.setIntegrationAmount(new BigDecimal("0"));
        order.setTotalAmount(payAmount);
        order.setPayAmount(payAmount);
        order.setUseIntegration(0);
        order.setMemberId(memberId);
        order.setMemberUsername(member.getUsername());
        order.setPromotionInfo("秒杀特惠活动");
        order.setCreateTime(new Date());
        order.setPayType(PayType.UNPAID.getType());
        order.setSourceType(OrderSourceType.PC.getType());
        order.setStatus(OrderStatus.UNPAID.getStatus());
        order.setOrderType(OrderType.SEC_KILL.getType());
        order.setConfirmStatus(OrderConfirmStatus.UNCONFIRMED.getStatus());
        order.setDeleteStatus(OrderDeleteStatus.NORMAL.getStatus());

        // 用户收货信息
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDetailAddress(address.getDetailAddress());

        // 赠送积分
        order.setIntegration(product.getGiftPoint());
        // 赠送成长值
        order.setGrowth(product.getGiftGrowth());

        /* -------------- 异步下单 -------------- */
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrder(order);
        orderMessage.setOrderItem(orderItem);
        orderMessage.setFlashPromotionRelationId(product.getFlashPromotionRelationId());
        orderMessage.setFlashPromotionLimit(product.getFlashPromotionLimit());
        orderMessage.setFlashPromotionEndDate(product.getFlashPromotionEndDate());

        Map<String, Object> result = new HashMap<>();
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        result.put("order", order);
        result.put("orderItemList", orderItemList);

        try {
            boolean sendStatus = orderMessageSender.sendCreateOrderMessage(orderMessage);
            if (sendStatus) {
                // 打上排队标记
                redisOpsUtil.set(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + memberId + ":" + productId,
                        Integer.toString(1), 60, TimeUnit.SECONDS);
                // 下单方式：0->同步下单；1->异步下单；-1->秒杀失败
                result.put("orderStatus", 1);
            }
        } catch (Exception e) {
            log.error("消息发送失败：error msg：{}", e.getMessage(), e.getCause());

            // 还原预减库存
            incrRedisStock(productId);
            // 清除掉本地缓存已经售罄的标记
            cache.remove(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId);
            // 通知服务集群，清除本地售罄标记
            if (shouldPublishCleanMsg(productId)) {
                redisOpsUtil.publish("cleanNoStockCache", productId);
            }

            result.put("orderStatus", -1);
            return CommonResult.failed(result, "下单失败");
        }

        return CommonResult.success(result, "下单中...");
    }

    @Override
    public void incrRedisStock(Long productId) {
        if (redisOpsUtil.hasKey(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId)) {
            redisOpsUtil.increment(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId);
        }
    }

    @Override
    public boolean shouldPublishCleanMsg(Long productId) {
        Integer stock = redisOpsUtil.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId, Integer.class);
        return (stock == null || stock <= 0);
    }

    @Override
    public Long asyncCreateOrder(OmsOrder order, OmsOrderItem orderItem, Long flashPromotionRelationId) {
        // 扣减库存
        Integer result = secKillStockDAO.decrStock(flashPromotionRelationId, 1);
        if (result <= 0) {
            throw new RuntimeException("没抢到！");
        }

        // 插入订单记录
        orderMapper.insertSelective(order);
        // OrderItem 关联
        orderItem.setOrderId(order.getId());
        orderItem.setOrderSn(order.getOrderSn());
        orderItemMapper.insertSelective(orderItem);

        // 创建订单成功，发送定时消失：15 min 后如果没有支付，则取消当前订单，释放库存
        try {
            boolean sendStatus = orderMessageSender.sendTimeoutOrderMessage(order.getId() + ":" + flashPromotionRelationId + ":" + orderItem.getId());
            if (!sendStatus) {
                throw new RuntimeException("订单超时取消消息发送失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("订单超时取消消息发送失败！");
        }
        return order.getId();
    }

    @Override
    public PmsProductParam getProductInfo(Long productId) {
        // 获取秒杀商品信息
        return pmsProductFeignApi.getProductInfo(productId).getData();
    }

    /**
     * 订单下单前的检查
     */
    private CommonResult confirmCheck(Long productId, Long memberId, String token) throws BusinessException {
        // 1. 获取售罄标记，检验商品是否已售罄
        Boolean localCache = cache.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId);
        if (localCache != null && localCache) {
            return CommonResult.failed("商品已售罄，请购买其他商品！");
        }

        // 2. 校验是否有资格购买
        String redisToken = redisOpsUtil.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + memberId + ":" + productId);
        if (StringUtils.isBlank(token) || !Objects.equals(redisToken, token)) {
            return CommonResult.failed("抱歉，您没有预约到购买资格哦～");
        }

        // 3. 从 Redis 缓存中取出当前要购买的商品库存
        Integer stock = redisOpsUtil.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId, Integer.class);
        if (stock == null || stock <= 0) {
            // 设置标记，如果售罄了在本地 cache 中设置为 true
            cache.set(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId, true);
            return CommonResult.failed("商品已售罄，请购买其他商品！");
        }

        // 4. 校验是否重复抢购
        String async = redisOpsUtil.get(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + memberId + ":" + productId);
        if (Objects.equals(async, "1")) {
            Map<String, Object> result = new HashMap<>();
            result.put("orderStatus", 1);
            return CommonResult.failed(result, "亲，已经在排队啦，请勿重复提交！");
        }

        return CommonResult.success(null);
    }

    /**
     * 验证秒杀商品是否在有效时间内
     * @return
     */
    private boolean validateSecKillTime(PmsProductParam product) {
        // 当前时间
        Date now = new Date();
        return product.getFlashPromotionStatus() != 1
                || product.getFlashPromotionStartDate() == null
                || product.getFlashPromotionEndDate() == null
                || now.before(product.getFlashPromotionStartDate())
                || now.after(product.getFlashPromotionEndDate());
    }

    /**
     * 计算总金额
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(PmsProductParam product) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal("0"));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        totalAmount = totalAmount.add(product.getFlashPromotionPrice()).multiply(new BigDecimal("1"));
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount);
        return calcAmount;
    }

    /**
     * Redis 预减库存
     */
    private boolean preDecrRedisStock(Long productId, Long promotionId) {
        Long stock = redisOpsUtil.decrement(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId);
        if (stock < 0) {
            // 还原库存
            incrRedisStock(productId);

            /*
             * 这里千万不能用 setNX，一旦使用，可能会出现如果 JVM 在消息发送出去之前就挂掉了，
             * 那么就意味着当前商品库存没有办法在卖完后跟 DB 做同步。
             */
            if (!redisOpsUtil.hasKey(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + promotionId)) {
                /*
                 * 目的：确保不会发生少卖现象
                 * 发送延时消息：60s 后，同步一次库存；高并发下可能会发送多条延时消息，但是可以容忍
                 */
                if (orderMessageSender.sendStockSyncMessage(productId, promotionId)) {
                    redisOpsUtil.set(RedisKeyPrefixConst.STOCK_REFRESHED_MESSAGE_PREFIX + promotionId, 0);
                }
            }
            return false;
        }
        return true;
    }
}
