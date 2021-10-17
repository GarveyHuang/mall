package com.shura.mall.domain;

import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberLevel;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description:
 */
public class PortalMemberInfo extends UmsMember {

    private UmsMemberLevel umsMemberLevel;

    public UmsMemberLevel getUmsMemberLevel() {
        return umsMemberLevel;
    }

    public void setUmsMemberLevel(UmsMemberLevel umsMemberLevel) {
        this.umsMemberLevel = umsMemberLevel;
    }
}
