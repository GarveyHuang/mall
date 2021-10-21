package com.shura.mall.domain;

import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description:
 */
@Getter
@Setter
public class PortalMemberInfo extends UmsMember {

    private UmsMemberLevel umsMemberLevel;
}
