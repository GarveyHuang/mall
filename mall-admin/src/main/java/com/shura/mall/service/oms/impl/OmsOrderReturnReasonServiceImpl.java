package com.shura.mall.service.oms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.mapper.OmsOrderReturnReasonMapper;
import com.shura.mall.model.oms.OmsOrderReturnReason;
import com.shura.mall.model.oms.OmsOrderReturnReasonExample;
import com.shura.mall.service.oms.IOmsOrderReturnReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 订单退货原因管理 Service 实现类
 */
@Service("orderReturnReasonService")
public class OmsOrderReturnReasonServiceImpl implements IOmsOrderReturnReasonService {

    @Autowired
    private OmsOrderReturnReasonMapper orderReturnReasonMapper;

    @Override
    public int create(OmsOrderReturnReason returnReason) {
        returnReason.setCreateTime(new Date());
        return orderReturnReasonMapper.insert(returnReason);
    }

    @Override
    public int update(Long id, OmsOrderReturnReason returnReason) {
        returnReason.setId(id);
        return orderReturnReasonMapper.updateByPrimaryKey(returnReason);
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrderReturnReasonExample example = new OmsOrderReturnReasonExample();
        example.createCriteria().andIdIn(ids);
        return orderReturnReasonMapper.deleteByExample(example);
    }

    @Override
    public List<OmsOrderReturnReason> list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);

        OmsOrderReturnReasonExample example = new OmsOrderReturnReasonExample();
        example.setOrderByClause("sort desc");
        return orderReturnReasonMapper.selectByExample(example);
    }

    @Override
    public int updateStatus(List<Long> ids, Integer status) {
        if(!status.equals(0) && !status.equals(1)){
            return 0;
        }

        OmsOrderReturnReason returnReason = new OmsOrderReturnReason();
        returnReason.setStatus(status);

        OmsOrderReturnReasonExample example = new OmsOrderReturnReasonExample();
        example.createCriteria().andIdIn(ids);
        return orderReturnReasonMapper.updateByExampleSelective(returnReason,example);
    }

    @Override
    public OmsOrderReturnReason getItem(Long id) {
        return orderReturnReasonMapper.selectByPrimaryKey(id);
    }
}
