package com.shura.mall.service.impl;

import com.shura.mall.common.exception.BusinessException;
import com.shura.mall.domain.CartProduct;
import com.shura.mall.domain.CartPromotionItem;
import com.shura.mall.feignapi.pms.PmsProductFeignApi;
import com.shura.mall.mapper.OmsCartItemMapper;
import com.shura.mall.model.ums.OmsCartItem;
import com.shura.mall.model.ums.OmsCartItemExample;
import com.shura.mall.service.OmsCartItemService;
import com.shura.mall.service.OmsPromotionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: 购物车管理 Service 实现类
 */
@Service("cartItemService")
public class OmsCartItemServiceImpl implements OmsCartItemService {

    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Autowired
    private OmsPromotionService promotionService;

    @Autowired
    private PmsProductFeignApi productFeignApi;

    @Override
    public int add(OmsCartItem cartItem, Long memberId, String nickName) {
        int count;
        cartItem.setMemberId(memberId);
        cartItem.setMemberNickname(nickName);
        cartItem.setDeleteStatus(0);
        OmsCartItem existCarItem = getCartItem(cartItem);
        if (existCarItem == null) {
            // 创建购物车
            cartItem.setCreateTime(new Date());
            // TODO 查询商品信息需要远程调用商品服务获取，这里暂时不处理
            CartProduct cartProduct = productFeignApi.getCartProduct(cartItem.getProductId()).getData();

            cartItem.setProductName(cartProduct.getName());
            cartItem.setPrice(cartProduct.getPrice());
            cartItem.setProductPic(cartProduct.getPic());
            cartItem.setProductBrand(cartProduct.getBrandName());
            cartItem.setProductCategoryId(cartProduct.getProductCategoryId());
            cartItem.setProductSn(cartProduct.getProductSn());
            cartItem.setProductSubTitle(cartProduct.getSubTitle());
            // 遍历商品 sku，设置购买价格
            cartProduct.getSkuStockList().stream()
                    .filter(skuItem -> Objects.equals(cartItem.getProductSkuId(), skuItem.getId()))
                    .forEach(skuItem -> {
                cartItem.setSp1(skuItem.getSp1());
                cartItem.setSp2(skuItem.getSp2());
                cartItem.setSp3(skuItem.getSp3());
                cartItem.setProductPic(skuItem.getPic());
                cartItem.setProductPic(skuItem.getPic());
                cartItem.setProductSkuCode(skuItem.getSkuCode());

                // TODO productAttr 需要优化，暂时不处理
            });
            count = cartItemMapper.insert(cartItem);
        } else {
            cartItem.setModifyTime(new Date());
            existCarItem.setQuantity(existCarItem.getQuantity() + cartItem.getQuantity());
            count = cartItemMapper.updateByPrimaryKey(existCarItem);
        }
        return count;
    }

    /**
     * 根据会员 id，商品 id 和规格获取购物车中的商品
     * @return
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        OmsCartItemExample.Criteria criteria = example.createCriteria()
                .andMemberIdEqualTo(cartItem.getMemberId())
                .andProductIdEqualTo(cartItem.getProductCategoryId())
                .andDeleteStatusEqualTo(0);

        if (StringUtils.isNotBlank(cartItem.getSp1())) {
            criteria.andSp1EqualTo(cartItem.getSp1());
        }

        if (StringUtils.isNotBlank(cartItem.getSp2())) {
            criteria.andSp2EqualTo(cartItem.getSp2());
        }

        if (StringUtils.isNotBlank(cartItem.getSp3())) {
            criteria.andSp3EqualTo(cartItem.getSp3());
        }

        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(cartItemList)) {
            return cartItemList.get(0);
        }

        return null;
    }

    @Override
    public Long cartItemCount() {
        return cartItemMapper.countByExample(new OmsCartItemExample());
    }

    @Override
    public List<OmsCartItem> list(Long memberId) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0).andMemberIdEqualTo(memberId);
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public List<CartPromotionItem> listSelectedPromotion(Long memberId, List<Long> itemIds) throws BusinessException {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andMemberIdEqualTo(memberId)
                .andIdIn(itemIds);
        List<OmsCartItem> cartItemList = cartItemMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(cartItemList)) {
            throw new BusinessException("没有选择购物车购买的商品！");
        }

        return promotionService.calcCartPromotion(cartItemList);
    }

    @Override
    public List<CartPromotionItem> listPromotion(Long memberId) {
        List<OmsCartItem> cartItemList = list(memberId);
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cartItemList)) {
            cartPromotionItemList = promotionService.calcCartPromotion(cartItemList);
        }
        return cartPromotionItemList;
    }

    @Override
    public int updateQuantity(Long id, Long memberId, Integer quantity) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria()
                .andDeleteStatusEqualTo(0)
                .andIdEqualTo(id)
                .andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public int delete(Long memberId, List<Long> ids) {
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productFeignApi.getCartProduct(productId).getData();
    }

    @Override
    public int updateAttr(OmsCartItem cartItem, Long memberId, String nickname) {
        // 删除原购物车信息
        OmsCartItem updateCart = new OmsCartItem();
        updateCart.setId(cartItem.getId());
        updateCart.setModifyTime(new Date());
        updateCart.setDeleteStatus(1);
        cartItemMapper.updateByPrimaryKeySelective(updateCart);

        cartItem.setId(null);
        add(cartItem, memberId, nickname);
        return 1;
    }

    @Override
    public int clear(Long memberId) {
        OmsCartItem item = new OmsCartItem();
        item.setDeleteStatus(1);
        item.setModifyTime(new Date());
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        return cartItemMapper.updateByExampleSelective(item, example);
    }
}
