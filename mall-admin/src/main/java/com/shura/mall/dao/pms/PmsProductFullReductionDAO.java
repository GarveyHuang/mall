package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsProductFullReduction;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品满减自定义 DAO
 */
public interface PmsProductFullReductionDAO {

    int insertList(@Param("list") List<PmsProductFullReduction> productFullReductionList);
}
