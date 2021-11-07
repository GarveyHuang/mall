package com.shura.mall.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: 本地缓存管理工具，采用 LRU 淘汰策略
 */
@Slf4j
@Component
public class LocalCache<T> {

    private Cache<String, T> localCache = null;

    @PostConstruct
    private void init() {
        localCache = CacheBuilder.newBuilder()
                // 设置本地缓存容量初始值
                .initialCapacity(10)
                // 设置本地缓存的最大容量
                .maximumSize(500)
                // 设置写缓存后多少秒过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    public void set(String key, T object) {
        localCache.put(key, object);
    }

    public <T> T get(String key) {
        return (T) localCache.getIfPresent(key);
    }

    public void remove(String key) {
        localCache.invalidate(key);
    }
}
