package com.shura.mall.util;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 布隆过滤器
 */
public class BloomFilterHelper<T> {

    private final int numHashFunctions;

    private final int bitSize;

    private final Funnel<T> funnel;

    public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
        Preconditions.checkArgument(funnel != null, "funnel 不能为空");
        this.funnel = funnel;
        // 计算 bit 数组长度
        bitSize = optimalNumOfBits(expectedInsertions, fpp);
        // 计算 hash 方法执行次数
        numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitSize);
    }

    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int)  hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash <= 0) {
                nextHash = ~nextHash;
            }

            offset[i - 1] = nextHash % bitSize;
        }

        return offset;
    }

    /**
     * 计算 bit 数组长度
     * @param n
     * @param p
     * @return
     */
    private int optimalNumOfBits(long n, double p) {
        if (p == 0) {
            // 设定最小数组长度
            p = Double.MIN_VALUE;
        }

        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * 计算 hash 方法执行次数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
}
