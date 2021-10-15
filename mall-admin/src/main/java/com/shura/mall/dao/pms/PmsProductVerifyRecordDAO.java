package com.shura.mall.dao.pms;

import com.shura.mall.model.pms.PmsProductVerifyRecord;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 商品审核日志自定义 DAO
 */
public interface PmsProductVerifyRecordDAO {

    int insertList(@Param("list") List<PmsProductVerifyRecord> list);
}
