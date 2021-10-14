package com.shura.mall.service.pms.impl;

import com.shura.mall.dto.pms.PmsProductParam;
import com.shura.mall.dto.pms.PmsProductQueryParam;
import com.shura.mall.dto.pms.PmsProductResult;
import com.shura.mall.mapper.PmsProductMapper;
import com.shura.mall.model.pms.PmsProduct;
import com.shura.mall.model.pms.PmsSkuStock;
import com.shura.mall.service.pms.IPmsProductService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        relateAndInsertList(productFullReducetionDAO, productParam.getProductFullReductionList(), productId);
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
        return null;
    }

    @Override
    public int update(Long id, PmsProductParam productParam) {
        return 0;
    }

    @Override
    public List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum) {
        return null;
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus, String detail) {
        return 0;
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        return 0;
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        return 0;
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        return 0;
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        return 0;
    }

    @Override
    public List<PmsProduct> list(String keyword) {
        return null;
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
