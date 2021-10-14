package com.shura.mall.dao.cms;

import com.shura.mall.model.cms.CmsPreferenceAreaProductRelation;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 优选和商品关系自定义 DAO
 */
public interface CmsPreferenceAreaProductRelationDAO {

    int insertList(@Param("list") List<CmsPreferenceAreaProductRelation> preferenceAreaProductRelationList);
}
