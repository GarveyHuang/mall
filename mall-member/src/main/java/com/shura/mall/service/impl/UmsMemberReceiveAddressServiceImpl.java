package com.shura.mall.service.impl;

import com.shura.mall.mapper.UmsMemberReceiveAddressMapper;
import com.shura.mall.model.ums.UmsMemberReceiveAddress;
import com.shura.mall.model.ums.UmsMemberReceiveAddressExample;
import com.shura.mall.service.IUmsMemberReceiveAddressService;
import com.shura.mall.service.IUmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 用户地址管理 Service 实现类
 */
@Service("memberReceiveAddressService")
public class UmsMemberReceiveAddressServiceImpl implements IUmsMemberReceiveAddressService {
    
    @Autowired
    private IUmsMemberService memberService;
    
    @Autowired
    private UmsMemberReceiveAddressMapper memberReceiveAddressMapper;

    @Override
    public int add(UmsMemberReceiveAddress address, Long memberId) {
        address.setMemberId(memberId);
        return memberReceiveAddressMapper.insert(address);
    }

    @Override
    public int delete(Long id,Long memberId) {
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(memberId).andIdEqualTo(id);
        return memberReceiveAddressMapper.deleteByExample(example);
    }

    @Override
    public int update(Long id, UmsMemberReceiveAddress address,Long memberId) {
        address.setId(null);
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(memberId).andIdEqualTo(id);
        return memberReceiveAddressMapper.updateByExampleSelective(address,example);
    }

    @Override
    public List<UmsMemberReceiveAddress> list(long memberId) {
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        return memberReceiveAddressMapper.selectByExample(example);
    }

    @Override
    public UmsMemberReceiveAddress getItem(Long id, long memberId) {
        UmsMemberReceiveAddressExample example = new UmsMemberReceiveAddressExample();
        example.createCriteria()/*.andMemberIdEqualTo(memberId)*/.andIdEqualTo(id);
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressMapper.selectByExample(example);

        if (!CollectionUtils.isEmpty(addressList)) {
            return addressList.get(0);
        }

        return null;
    }
}
