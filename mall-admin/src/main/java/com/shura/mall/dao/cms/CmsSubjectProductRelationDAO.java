package com.shura.mall.dao.cms;

import com.shura.mall.model.cms.CmsSubjectProductRelation;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品和专题关系自定义 DAO
 */
public interface CmsSubjectProductRelationDAO {

    int insertList(@Param("list") List<CmsSubjectProductRelation> subjectProductRelationList);
}
