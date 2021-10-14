package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsMemberPrice;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 会员价格自定义 DAO
 */
public interface PmsMemberPriceDAO {

    int insertList(@Param("list") List<PmsMemberPrice> memberPriceList);
}
