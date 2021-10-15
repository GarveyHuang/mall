package com.shura.mall.service;

import com.shura.mall.model.ums.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 用户地址管理 Service
 */
public interface IUmsMemberReceiveAddressService {

    /**
     * 添加收货地址
     */
    int add(UmsMemberReceiveAddress address, Long memberId);

    /**
     * 删除收货地址
     * @param id 地址表的id
     */
    int delete(Long id,Long memberId);

    /**
     * 修改收货地址
     * @param id 地址表的id
     * @param address 修改的收货地址信息
     */
    int update(Long id, UmsMemberReceiveAddress address,Long memberId);

    /**
     * 返回当前用户的收货地址
     */
    List<UmsMemberReceiveAddress> list(long memberId);

    /**
     * 获取地址详情
     * @param id 地址id
     */
    UmsMemberReceiveAddress getItem(Long id,long memberId);
}
