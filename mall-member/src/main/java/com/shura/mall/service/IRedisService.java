package com.shura.mall.service;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: Redis 操作 Service
 */
public interface IRedisService {

    /**
     * 存储数据
     */
    void set(String key, String value);

    /**
     * 获取数据
     */
    String get(String key);

    /**
     * 设置过期时间
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     */
    void remove(String key);

    /**
     * 自增操作
     * @param delta 自增步长
     */
    Long increment(String key, long delta);
}
