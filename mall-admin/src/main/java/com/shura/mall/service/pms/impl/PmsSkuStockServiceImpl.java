package com.shura.mall.service.pms.impl;

import com.shura.mall.dao.pms.PmsSkuStockDAO;
import com.shura.mall.mapper.PmsSkuStockMapper;
import com.shura.mall.model.pms.PmsSkuStock;
import com.shura.mall.model.pms.PmsSkuStockExample;
import com.shura.mall.service.pms.PmsSkuStockService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: sku 商品库存 Service 实现类
 */
@Service("skuStockService")
public class PmsSkuStockServiceImpl implements PmsSkuStockService {

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Autowired
    private PmsSkuStockDAO skuStockDAO;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria().andProductIdEqualTo(pid);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andSkuCodeLike(keyword + "%");
        }
        return skuStockMapper.selectByExample(example);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return skuStockDAO.replaceList(skuStockList);
    }
}
