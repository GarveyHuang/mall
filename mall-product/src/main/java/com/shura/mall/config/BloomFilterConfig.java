package com.shura.mall.config;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.component.BloomRedisService;
import com.shura.mall.service.IPmsProductService;
import com.shura.mall.util.BloomFilterHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 布隆过滤器配置
 */
@Slf4j
@Configuration
public class BloomFilterConfig implements InitializingBean {

    @Autowired
    private IPmsProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public BloomFilterHelper<String> initBloomFilterHelper() {
        return new BloomFilterHelper<>((Funnel<String>) (from, into) ->
                into.putString(from, Charsets.UTF_8)
                        .putString(from, Charsets.UTF_8), 1000000, 0.01);
    }

    @Bean
    public BloomRedisService bloomRedisService() {
        BloomRedisService bloomRedisService = new BloomRedisService();
        bloomRedisService.setBloomFilterHelper(initBloomFilterHelper());
        bloomRedisService.setRedisTemplate(redisTemplate);
        return bloomRedisService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Long> list = productService.getAllProductId();
        if (!CollectionUtils.isEmpty(list)) {
            log.info("加载商品到布隆过滤器当中，size: {}", list.size());

            for (Long item : list) {
                bloomRedisService().addByBloomFilter(RedisKeyPrefixConst.PRODUCT_REDIS_BLOOM_FILTER, item + "");
            }
        }
    }
}
