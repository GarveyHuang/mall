package com.shura.mall.domain;

import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberLevel;
import lombok.Data;

/**
 * @Author: Garvey
 * @Created: 2021/11/2
 * @Description:
 */
@Data
public class PortalMemberInfo extends UmsMember {

    /**
     * 会员等级信息
     */
    private UmsMemberLevel memberLevel;
}
