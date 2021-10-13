package com.shura.mall.dto.ums;

import com.shura.mall.model.ums.UmsPermission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description:
 */
public class UmsPermissionNode extends UmsPermission {

    @Getter
    @Setter
    private List<UmsPermissionNode> children;
}
