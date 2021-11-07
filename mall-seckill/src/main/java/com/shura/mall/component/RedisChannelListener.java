package com.shura.mall.component;

import com.shura.mall.common.constant.RedisKeyPrefixConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description: Redis channel 监听器
 */
@Slf4j
public class RedisChannelListener implements MessageListener {

    @Autowired
    private LocalCache localCache;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("sub message :) channel[cleanNoStockCache] !");
        String productId = new String(message.getBody(), StandardCharsets.UTF_8);
        localCache.remove(RedisKeyPrefixConst.SEC_KILL_STOCK_CACHE_PREFIX + productId);
    }
}
