package com.shura.mall.service.oms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.oms.OmsOrderReturnApplyDAO;
import com.shura.mall.dto.oms.OmsOrderReturnApplyResult;
import com.shura.mall.dto.oms.OmsReturnApplyQueryParam;
import com.shura.mall.dto.oms.OmsUpdateStatusParam;
import com.shura.mall.mapper.OmsOrderReturnApplyMapper;
import com.shura.mall.model.oms.OmsOrderReturnApply;
import com.shura.mall.model.oms.OmsOrderReturnApplyExample;
import com.shura.mall.service.oms.IOmsOrderReturnApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 退货申请管理 Service 实现类
 */
@Service("orderReturnApplyService")
public class OmsOrderReturnApplyServiceImpl implements IOmsOrderReturnApplyService {

    @Autowired
    private OmsOrderReturnApplyMapper orderReturnApplyMapper;

    @Autowired
    private OmsOrderReturnApplyDAO orderReturnApplyDAO;

    @Override
    public List<OmsOrderReturnApply> list(OmsReturnApplyQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        return orderReturnApplyDAO.getList(queryParam);
    }

    @Override
    public int delete(List<Long> ids) {
        OmsOrderReturnApplyExample example = new OmsOrderReturnApplyExample();
        example.createCriteria().andIdIn(ids).andStatusEqualTo(3);
        return orderReturnApplyMapper.deleteByExample(example);
    }

    @Override
    public int updateStatus(Long id, OmsUpdateStatusParam statusParam) {
        Integer status = statusParam.getStatus();
        OmsOrderReturnApply returnApply = new OmsOrderReturnApply();
        if (status.equals(1)) {
            //确认退货
            returnApply.setId(id);
            returnApply.setStatus(1);
            returnApply.setReturnAmount(statusParam.getReturnAmount());
            returnApply.setCompanyAddressId(statusParam.getCompanyAddressId());
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else if (status.equals(2)) {
            //完成退货
            returnApply.setId(id);
            returnApply.setStatus(2);
            returnApply.setReceiveTime(new Date());
            returnApply.setReceiveMan(statusParam.getReceiveMan());
            returnApply.setReceiveNote(statusParam.getReceiveNote());
        } else if (status.equals(3)) {
            //拒绝退货
            returnApply.setId(id);
            returnApply.setStatus(3);
            returnApply.setHandleTime(new Date());
            returnApply.setHandleMan(statusParam.getHandleMan());
            returnApply.setHandleNote(statusParam.getHandleNote());
        } else {
            return 0;
        }
        return orderReturnApplyMapper.updateByPrimaryKeySelective(returnApply);
    }

    @Override
    public OmsOrderReturnApplyResult getItem(Long id) {
        return orderReturnApplyDAO.getDetail(id);
    }
}
