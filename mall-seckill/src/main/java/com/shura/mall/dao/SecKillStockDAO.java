package com.shura.mall.dao;

import org.apache.ibatis.annotations.Param;

/**
 * @Author: Garvey
 * @Created: 2021/11/3
 * @Description: 秒杀库存自定义 DAO
 */
public interface SecKillStockDAO {

    /**
     * 扣减库存
     * @param id
     * @param stock
     * @return
     */
    Integer decrStock(@Param("id") Long id, @Param("stock") Integer stock);

    /**
     * 秒杀商品乐观锁扣减库存
     */
    Integer decrStockInVersion(@Param("id") Long id, @Param("oldStock") Integer oldStock, @Param("newStock") Integer newStock);

    /**
     * 查询当前的缓存库存
     */
    Integer selectSecKillStock(@Param("id") Long id);

    /* --------------- 悲观锁实现 --------------- */

    /**
     * 查询加锁
     */
    Integer selectSecKillStockInLock(@Param("id") Long id);

    /**
     * 秒杀商品加锁扣减库存
     */
    Integer decrStockInLock(@Param("id") Long id, @Param("stock") Integer stock);
}
