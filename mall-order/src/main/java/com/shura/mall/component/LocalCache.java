package com.shura.mall.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.shura.mall.domain.PmsProductParam;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 本地缓存管理工具，采用 LRU 淘汰策略
 */
@Component
public class LocalCache {

    private Cache<String, PmsProductParam> localCache = null;

    @PostConstruct
    private void init() {
        localCache = CacheBuilder.newBuilder()
                // 设置本地缓存容器的初始容量
                .initialCapacity(10)
                // 设置本地缓存容器的最大容量
                .maximumSize(500)
                // 设置写缓存后多少秒过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    public void setLocalCache(String key, PmsProductParam object) {
        localCache.put(key, object);
    }

    public <T> T getCache(String key) {
        return (T) localCache.getIfPresent(key);
    }

    public void remove(String key) {
        localCache.invalidate(key);
    }
}