package com.shura.mall.service.pms.impl;

import com.github.pagehelper.PageHelper;
import com.shura.mall.dao.cms.CmsPreferenceAreaProductRelationDAO;
import com.shura.mall.dao.cms.CmsSubjectProductRelationDAO;
import com.shura.mall.dao.pms.*;
import com.shura.mall.dto.pms.PmsProductParam;
import com.shura.mall.dto.pms.PmsProductQueryParam;
import com.shura.mall.dto.pms.PmsProductResult;
import com.shura.mall.mapper.*;
import com.shura.mall.model.cms.CmsPreferenceAreaProductRelationExample;
import com.shura.mall.model.cms.CmsSubjectProductRelationExample;
import com.shura.mall.model.pms.*;
import com.shura.mall.service.pms.IPmsProductService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/14
 * @Description: 商品管理 Service 实现类
 */
@Service("productService")
public class PmsProductServiceImpl implements IPmsProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmsProductServiceImpl.class);

    @Autowired
    private PmsProductMapper productMapper;

    @Autowired
    private PmsProductDAO productDAO;

    @Autowired
    private PmsMemberPriceMapper memberPriceMapper;

    @Autowired
    private PmsMemberPriceDAO memberPriceDAO;

    @Autowired
    private PmsProductLadderMapper productLadderMapper;

    @Autowired
    private PmsProductLadderDAO productLadderDAO;

    @Autowired
    private PmsProductFullReductionMapper productFullReductionMapper;

    @Autowired
    private PmsProductFullReductionDAO productFullReductionDAO;

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    private PmsSkuStockDAO skuStockDAO;

    @Autowired
    private PmsProductAttributeValueMapper productAttributeValueMapper;

    @Autowired
    private PmsProductAttributeValueDAO productAttributeValueDAO;

    @Autowired
    private PmsProductVerifyRecordDAO productVerifyRecordDAO;

    @Autowired
    private CmsSubjectProductRelationMapper subjectProductRelationMapper;

    @Autowired
    private CmsSubjectProductRelationDAO subjectProductRelationDAO;

    @Autowired
    private CmsPreferenceAreaProductRelationMapper preferenceAreaProductRelationMapper;

    @Autowired
    private CmsPreferenceAreaProductRelationDAO preferenceAreaProductRelationDAO;

    @Override
    public int create(PmsProductParam productParam) {
        // 创建商品
        PmsProduct product = productParam;
        product.setId(null);
        productMapper.insertSelective(product);

        // 根据促销类型设置价格、阶梯价格、满减价格
        Long productId = product.getId();
        // 会员价格
        relateAndInsertList(memberPriceDAO, productParam.getMemberPriceList(), productId);
        // 阶梯价格
        relateAndInsertList(productLadderDAO, productParam.getProductLadderList(), productId);
        // 满减价格
        relateAndInsertList(productFullReductionDAO, productParam.getProductFullReductionList(), productId);
        // 处理 sku 的编码
        handleSkuStockCode(productParam.getSkuStockList(), productId);
        // 添加 sku 库存信息
        relateAndInsertList(skuStockDAO, productParam.getSkuStockList(), productId);
        // 添加商品参数，添加自定义商品规格
        relateAndInsertList(productAttributeValueDAO, productParam.getProductAttributeValueList(), productId);
        // 关联专题
        relateAndInsertList(subjectProductRelationDAO, productParam.getSubjectProductRelationList(), productId);
        // 关联优选
        relateAndInsertList(preferenceAreaProductRelationDAO, productParam.getPreferenceAreaProductRelationList(), productId);

        return 1;
    }

    @Override
    public PmsProductResult getUpdateInfo(Long id) {
        return productDAO.getUpdateInfo(id);
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        // 更新商品信息
        PmsProduct product = productParam;
        product.setId(id);
        productMapper.updateByPrimaryKeySelective(product);

        // 更新会员价格
        PmsMemberPriceExample memberPriceExample = new PmsMemberPriceExample();
        memberPriceExample.createCriteria().andProductIdEqualTo(id);
        memberPriceMapper.deleteByExample(memberPriceExample);
        relateAndInsertList(memberPriceDAO, productParam.getMemberPriceList(), id);

        // 更新阶梯价格
        PmsProductLadderExample productLadderExample = new PmsProductLadderExample();
        productLadderExample.createCriteria().andProductIdEqualTo(id);
        productLadderMapper.deleteByExample(productLadderExample);
        relateAndInsertList(productLadderDAO, productParam.getProductLadderList(), id);

        // 更新满减价格
        PmsProductFullReductionExample fullReductionExample = new PmsProductFullReductionExample();
        fullReductionExample.createCriteria().andProductIdEqualTo(id);
        productFullReductionMapper.deleteByExample(fullReductionExample);
        relateAndInsertList(productFullReductionDAO, productParam.getProductFullReductionList(), id);

        // 更新 sku 库存信息
        PmsSkuStockExample skuStockExample = new PmsSkuStockExample();
        skuStockExample.createCriteria().andProductIdEqualTo(id);
        skuStockMapper.deleteByExample(skuStockExample);
        handleSkuStockCode(productParam.getSkuStockList(), id);
        relateAndInsertList(skuStockDAO, productParam.getSkuStockList(), id);

        // 更新商品参数、自定义商品规格
        PmsProductAttributeValueExample attributeValueExample = new PmsProductAttributeValueExample();
        attributeValueExample.createCriteria().andProductIdEqualTo(id);
        productAttributeValueMapper.deleteByExample(attributeValueExample);
        relateAndInsertList(productAttributeValueDAO, productParam.getProductAttributeValueList(), id);

        // 更新关联专题
        CmsSubjectProductRelationExample subjectProductRelationExample = new CmsSubjectProductRelationExample();
        subjectProductRelationExample.createCriteria().andProductIdEqualTo(id);
        subjectProductRelationMapper.deleteByExample(subjectProductRelationExample);
        relateAndInsertList(subjectProductRelationDAO, productParam.getSubjectProductRelationList(), id);

        // 更新关联优选
        CmsPreferenceAreaProductRelationExample preferenceAreaProductRelationExample = new CmsPreferenceAreaProductRelationExample();
        preferenceAreaProductRelationExample.createCriteria().andProductIdEqualTo(id);
        preferenceAreaProductRelationMapper.deleteByExample(preferenceAreaProductRelationExample);
        relateAndInsertList(preferenceAreaProductRelationDAO, productParam.getPreferenceAreaProductRelationList(), id);

        return 1;
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);

        PmsProductExample productExample = new PmsProductExample();
        PmsProductExample.Criteria criteria = productExample.createCriteria();
        criteria.andDeleteStatusEqualTo(0);

        if (Objects.nonNull(productQueryParam.getPublishStatus())) {
            criteria.andPublishStatusEqualTo(productQueryParam.getPublishStatus());
        }

        if (Objects.nonNull(productQueryParam.getVerifyStatus())) {
            criteria.andVerifyStatusEqualTo(productQueryParam.getVerifyStatus());
        }

        if (StringUtils.isNotBlank(productQueryParam.getKeyword())) {
            criteria.andNameLike(productQueryParam.getKeyword() + "%");
        }

        if (StringUtils.isNotBlank(productQueryParam.getProductSn())) {
            criteria.andProductSnEqualTo(productQueryParam.getProductSn());
        }

        if (Objects.nonNull(productQueryParam.getBrandId())) {
            criteria.andBrandIdEqualTo(productQueryParam.getBrandId());
        }

        if (Objects.nonNull(productQueryParam.getProductCategoryId())) {
            criteria.andProductCategoryIdEqualTo(productQueryParam.getProductCategoryId());
        }

        return productMapper.selectByExample(productExample);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        PmsProduct product = new PmsProduct();
        product.setVerifyStatus(verifyStatus);

        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        List<PmsProductVerifyRecord> list = new ArrayList<>();
        int count = productMapper.updateByExampleSelective(product, example);

        // 修改完审核状态后，插入审核记录
        for (Long id : ids) {
            PmsProductVerifyRecord record = new PmsProductVerifyRecord();
            record.setProductId(id);
            record.setCreateTime(new Date());
            record.setDetail(detail);
            record.setStatus(verifyStatus);
            record.setVerifyMan("test");
            list.add(record);
        }
        productVerifyRecordDAO.insertList(list);

        return count;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        PmsProduct product = new PmsProduct();
        product.setPreviewStatus(publishStatus);

        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product, example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        PmsProduct product = new PmsProduct();
        product.setRecommendStatus(recommendStatus);

        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product, example);
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        PmsProduct product = new PmsProduct();
        product.setNewStatus(newStatus);

        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product, example);
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        PmsProduct product = new PmsProduct();
        product.setDeleteStatus(deleteStatus);

        PmsProductExample example = new PmsProductExample();
        example.createCriteria().andIdIn(ids);
        return productMapper.updateByExampleSelective(product, example);
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        PmsProductExample example = new PmsProductExample();
        PmsProductExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andNameLike(keyword + "%");
            example.or().andDeleteStatusEqualTo(0).andProductSnLike(keyword +"%");
        }
        return productMapper.selectByExample(example);
    }

    /**
     * 建立和插入关系表操作
     * @param dao       可操作的 dao
     * @param dataList  要插入的数据
     * @param productId 建立关系的 id
     */
    private void relateAndInsertList(Object dao, List dataList, Long productId) {
        try {
            if (CollectionUtils.isEmpty(dataList)) {
                return;
            }

            for (Object item : dataList) {
                Method setId = item.getClass().getMethod("setId", Long.class);
                setId.invoke(item, (Long) null);
                Method setProductId = item.getClass().getMethod("setProductId", Long.class);
                setProductId.invoke(item, productId);
            }

            Method insertList = dao.getClass().getMethod("insertList", List.class);
            insertList.invoke(dao, insertList);
        } catch (Exception e) {
            LOGGER.error("创建产品出错：{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void handleSkuStockCode(List<PmsSkuStock> skuStockList, Long productId) {
        if (CollectionUtils.isEmpty(skuStockList)) {
            return;
        }

        for (int i = 0; i < skuStockList.size(); i++) {
            PmsSkuStock skuStock = skuStockList.get(i);
            if (StringUtils.isBlank(skuStock.getSkuCode())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                StringBuilder sb = new StringBuilder();
                // 日期
                sb.append(sdf.format(new Date()));
                // 四位商品 id
                sb.append(String.format("%04d", productId));
                // 三位索引 id
                sb.append(String.format("%03d", i + 1));

                skuStock.setSkuCode(sb.toString());
            }
        }
    }
}
