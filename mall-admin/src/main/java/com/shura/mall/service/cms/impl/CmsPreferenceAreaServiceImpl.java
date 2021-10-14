package com.shura.mall.service.cms.impl;

import com.shura.mall.mapper.CmsPreferenceAreaMapper;
import com.shura.mall.model.cms.CmsPreferenceArea;
import com.shura.mall.model.cms.CmsPreferenceAreaExample;
import com.shura.mall.service.cms.ICmsPreferenceAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 优选专区 Service 实现类
 */
@Service("preferenceAreaService")
public class CmsPreferenceAreaServiceImpl implements ICmsPreferenceAreaService {

    @Autowired
    private CmsPreferenceAreaMapper preferenceAreaMapper;

    @Override
    public List<CmsPreferenceArea> listAll() {
        return preferenceAreaMapper.selectByExample(new CmsPreferenceAreaExample());
    }
}
