package com.shura.mall.dao.pms;

import com.shura.mall.dto.pms.PmsProductResult;
import org.springframework.data.repository.query.Param;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品自定义 DAO
 */
public interface PmsProductDAO {

    /**
     * 获取商品编辑信息
     */
    PmsProductResult getUpdateInfo(@Param("id") Long id);
}
