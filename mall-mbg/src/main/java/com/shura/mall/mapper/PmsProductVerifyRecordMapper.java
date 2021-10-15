package com.shura.mall.mapper;

import com.shura.mall.model.pms.PmsProductVerifyRecord;
import com.shura.mall.model.pms.PmsProductVerifyRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PmsProductVerifyRecordMapper {
    long countByExample(PmsProductVerifyRecordExample example);

    int deleteByExample(PmsProductVerifyRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PmsProductVerifyRecord record);

    int insertSelective(PmsProductVerifyRecord record);

    List<PmsProductVerifyRecord> selectByExample(PmsProductVerifyRecordExample example);

    PmsProductVerifyRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PmsProductVerifyRecord record, @Param("example") PmsProductVerifyRecordExample example);

    int updateByExample(@Param("record") PmsProductVerifyRecord record, @Param("example") PmsProductVerifyRecordExample example);

    int updateByPrimaryKeySelective(PmsProductVerifyRecord record);

    int updateByPrimaryKey(PmsProductVerifyRecord record);
}