package com.shura.mall.service.cms;

import com.shura.mall.model.cms.CmsSubject;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 商品专题 Service
 */
public interface CmsSubjectService {

    /**
     * 查询所有专题
     */
    List<CmsSubject> listAll();

    /**
     * 分页查询专题
     */
    List<CmsSubject> list(String keyword, Integer pageNum, Integer pageSize);
}
