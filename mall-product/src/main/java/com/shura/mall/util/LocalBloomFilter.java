package com.shura.mall.util;

import org.apache.curator.shaded.com.google.common.hash.BloomFilter;
import org.apache.curator.shaded.com.google.common.hash.Funnels;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 布隆过滤器 - 存储在当前 JVM 中
 */
public class LocalBloomFilter {

    private static final BloomFilter<String> bloomFilter =
            BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8), 1000000, 0.01);

    public static boolean match(String id) {
        return bloomFilter.mightContain(id);
    }

    public static void put(Long id) {
        bloomFilter.put(id + "");
    }
}
