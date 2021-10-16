package com.shura.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.component.LocalCache;
import com.shura.mall.component.lock.ZookeeperLock;
import com.shura.mall.dao.FlashPromotionProductDAO;
import com.shura.mall.dao.PortalProductDAO;
import com.shura.mall.domain.*;
import com.shura.mall.mapper.SmsFlashPromotionMapper;
import com.shura.mall.mapper.SmsFlashPromotionSessionMapper;
import com.shura.mall.model.sms.*;
import com.shura.mall.service.IPmsProductService;
import com.shura.mall.util.DateUtil;
import com.shura.mall.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 商品详情信息 Service 实现类
 */
@Slf4j
@Service("productService")
public class PmsProductServiceImpl implements IPmsProductService {

    @Autowired
    private PortalProductDAO portalProductDAO;

    @Autowired
    private FlashPromotionProductDAO flashPromotionProductDAO;

    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;

    @Autowired
    private SmsFlashPromotionSessionMapper promotionSessionMapper;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private LocalCache localCache;

    @Autowired
    private ZookeeperLock zookeeperLock;
    private final String LOCK_PATH = "/load_db_";

    private Map<String, PmsProductParam> cacheMap = new ConcurrentHashMap<>();

    /**
     * 获取商品详情信息（分布式锁、本地缓存、redis 缓存）
     * @param id 产品ID
     * @return
     */
    @Override
    public PmsProductParam getProductInfo(Long id) {
        // 1. 从本地缓存获取
        PmsProductParam productInfo = localCache.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id);
        if (productInfo != null) {
            return productInfo;
        }

        // 2. 从 Redis 缓存获取
        productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
        if (productInfo != null) {
            log.info("get redis productId: " + productInfo);
            // 放入本地缓存
            localCache.setLocalCache(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo);
            return productInfo;
        }

        // 3. 加锁，从数据库中获取
        try {
            if (zookeeperLock.lock(LOCK_PATH + id)) {
                productInfo = portalProductDAO.getProductInfo(id);
                if (productInfo == null) {
                    return null;
                }

                checkFlash(id, productInfo);
                log.info("set db productId: " + productInfo);
                redisOpsUtil.set(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo, 3600, TimeUnit.SECONDS);
                localCache.setLocalCache(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo);
            } else {
                log.info("again get redis productId: {}", productInfo);

                productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
                if (productInfo != null) {
                    localCache.setLocalCache(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo);
                }
            }
        } finally {
            log.info("unlock: " + productInfo);
            zookeeperLock.unlock(LOCK_PATH + id);
        }

        return productInfo;
    }

    private void checkFlash(Long id, PmsProductParam productInfo) {
        FlashPromotionParam promotion = flashPromotionProductDAO.getFlashPromotion(id);
        if (promotion != null) {
            productInfo.setFlashPromotionCount(promotion.getRelationList().get(0).getFlashPromotionCount());
            productInfo.setFlashPromotionLimit(promotion.getRelationList().get(0).getFlashPromotionLimit());
            productInfo.setFlashPromotionPrice(promotion.getRelationList().get(0).getFlashPromotionPrice());
            productInfo.setFlashPromotionRelationId(promotion.getRelationList().get(0).getId());
            productInfo.setFlashPromotionStartDate(promotion.getStartDate());
            productInfo.setFlashPromotionEndDate(promotion.getEndDate());
            productInfo.setFlashPromotionStatus(promotion.getStatus());
        }
    }

    /**
     * 获取秒杀商品列表
     * @param pageSize 页大小
     * @param pageNum 页号
     * @param flashPromotionId 秒杀活动ID，关联秒杀活动设置
     * @param sessionId 场次活动ID，for example：13:00-14:00 场等
     * @return
     */
    @Override
    public List<FlashPromotionProduct> getFlashProductList(Integer pageSize, Integer pageNum, Long flashPromotionId, Long sessionId) {
        PageHelper.startPage(pageNum, pageSize, "sort desc");

        return flashPromotionProductDAO.getFlashProductList(flashPromotionId, sessionId);
    }

    /**
     * 获取当前日期所有秒杀活动场次
     * @return
     */
    @Override
    public List<FlashPromotionSessionExt> getFlashPromotionSessionList() {
        Date now = new Date();
        SmsFlashPromotion promotion = getFlashPromotion(now);
        if (promotion != null) {
            SmsFlashPromotionSessionExample sessionExample = new SmsFlashPromotionSessionExample();
            // 获取时间段内的秒杀场次
            sessionExample.createCriteria().andStatusEqualTo(1);
            sessionExample.setOrderByClause("start_time asc");
            List<SmsFlashPromotionSession> promotionSessionList = promotionSessionMapper.selectByExample(sessionExample);
            List<FlashPromotionSessionExt> extList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(promotionSessionList)) {
                for (SmsFlashPromotionSession session : promotionSessionList) {
                    FlashPromotionSessionExt ext = new FlashPromotionSessionExt();
                    BeanUtils.copyProperties(session, ext);
                    ext.setFlashPromotionId(promotion.getId());

                    if (DateUtil.getTime(now).after(DateUtil.getTime(ext.getStartTime()))
                            && DateUtil.getDate(now).before(DateUtil.getTime(ext.getEndTime()))) {
                        // 活动进行中
                        ext.setSessionStatus(0);
                    } else if (DateUtil.getTime(now).before(DateUtil.getTime(ext.getStartTime()))) {
                        // 活动即将开始
                        ext.setSessionStatus(1);
                    } else if (DateUtil.getTime(now).after(DateUtil.getTime(ext.getEndTime()))) {
                        // 活动已结束
                        ext.setSessionStatus(2);
                    }

                    extList.add(ext);
                }

                return extList;
            }
        }

        return null;
    }

    /**
     * 根据时间获取秒杀活动
     * @param date
     * @return
     */
    private SmsFlashPromotion getFlashPromotion(Date date) {
        Date currentDate = DateUtil.getDate(date);
        SmsFlashPromotionExample example = new SmsFlashPromotionExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andStartDateLessThan(currentDate)
                .andEndDateGreaterThan(currentDate);
        List<SmsFlashPromotion> flashPromotionList = flashPromotionMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(flashPromotionList)) {
            return flashPromotionList.get(0);
        }

        return null;
    }

    /**
     * 获取首页的秒杀商品列表
     * @return
     */
    @Override
    public List<FlashPromotionProduct> getHomeSecKillProductList() {
        PageHelper.startPage(1, 8, "sort desc");

        FlashPromotionParam flashPromotionParam = flashPromotionProductDAO.getFlashPromotion(null);
        if (flashPromotionParam == null || CollectionUtils.isEmpty(flashPromotionParam.getRelationList())) {
            return null;
        }

        List<Long> promotionIds = flashPromotionParam.getRelationList().stream()
                .map(SmsFlashPromotionProductRelation::getId).collect(Collectors.toList());
        PageHelper.clearPage();
        return flashPromotionProductDAO.getHomePromotionProductList(promotionIds);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return portalProductDAO.getCartProduct(productId);
    }

    @Override
    public List<PromotionProduct> getPromotionProductList(List<Long> ids) {
        return portalProductDAO.getPromotionProductList(ids);
    }

    @Override
    public List<Long> getAllProductId() {
        return portalProductDAO.getAllProductId();
    }
}
