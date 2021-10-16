package com.shura.mall.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.dao.FlashPromotionProductDAO;
import com.shura.mall.domain.FlashPromotionParam;
import com.shura.mall.model.sms.SmsFlashPromotionProductRelation;
import com.shura.mall.util.RedisOpsUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: Redis 配置类
 */
@Configuration
public class RedisConfig implements InitializingBean {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 序列化后会产生 java 类型说明，如果不需要，用 "Jackson2JsonRedisSerializer" 和 "ObjectMapper" 配合效果更好
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Autowired
    private FlashPromotionProductDAO flashPromotionProductDAO;

    /**
     * 加载所有的秒杀活动商品库存到缓存 Redis 中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO 获取所有秒杀活动中的商品
        FlashPromotionParam promotion = flashPromotionProductDAO.getFlashPromotion(null);

        // TODO 如果没有秒杀活动，后面会报错
        if (promotion == null) {
            return;
        }

        Date now = new Date();
        // 结束时间
        Date endDate = promotion.getEndDate();
        // 剩余时间
        final long expired = endDate.getTime() - now.getTime();
        for (SmsFlashPromotionProductRelation item : promotion.getRelationList()) {
            redisOpsUtil().setIfAbsent(
                    RedisKeyPrefixConst.MIAOSHA_STOCK_CACHE + item.getProductId(),
                    item.getFlashPromotionCount(),
                    expired,
                    TimeUnit.SECONDS);
        }
    }

    @Bean
    public RedisOpsUtil redisOpsUtil() {
        return new RedisOpsUtil();
    }
}
