package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsProductLadder;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 会员阶梯价格自定义 DAO
 */
public interface PmsProductLadderDAO {

    int insertList(@Param("list") List<PmsProductLadder> pmsProductLadderList);
}
