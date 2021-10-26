package com.shura.mall.service.sms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.sms.SmsFlashPromotionProductRelationDAO;
import com.shura.mall.domain.sms.SmsFlashPromotionProductResult;
import com.shura.mall.mapper.SmsFlashPromotionProductRelationMapper;
import com.shura.mall.model.sms.SmsFlashPromotionProductRelation;
import com.shura.mall.model.sms.SmsFlashPromotionProductRelationExample;
import com.shura.mall.service.sms.SmsFlashPromotionProductRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 限时购商品关联管理 Service 实现类
 */
@Service("flashPromotionProductRelationService")
public class SmsFlashPromotionProductRelationServiceImpl implements SmsFlashPromotionProductRelationService {

    @Autowired
    private SmsFlashPromotionProductRelationMapper flashPromotionProductRelationMapper;

    @Autowired
    private SmsFlashPromotionProductRelationDAO flashPromotionProductRelationDAO;

    @Override
    public int create(List<SmsFlashPromotionProductRelation> relationList) {
        for (SmsFlashPromotionProductRelation relation : relationList) {
            flashPromotionProductRelationMapper.insert(relation);
        }

        return relationList.size();
    }

    @Override
    public int update(Long id, SmsFlashPromotionProductRelation relation) {
        relation.setId(id);
        return flashPromotionProductRelationMapper.updateByPrimaryKey(relation);
    }

    @Override
    public int delete(Long id) {
        return flashPromotionProductRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public SmsFlashPromotionProductRelation getItem(Long id) {
        return flashPromotionProductRelationMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmsFlashPromotionProductResult> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);

        return flashPromotionProductRelationDAO.getList(flashPromotionId,flashPromotionSessionId);
    }

    @Override
    public long getCount(Long flashPromotionId, Long flashPromotionSessionId) {
        SmsFlashPromotionProductRelationExample example = new SmsFlashPromotionProductRelationExample();
        example.createCriteria()
                .andFlashPromotionIdEqualTo(flashPromotionId)
                .andFlashPromotionSessionIdEqualTo(flashPromotionSessionId);
        return flashPromotionProductRelationMapper.countByExample(example);
    }
}
