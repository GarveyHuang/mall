package com.shura.mall.dto.converter;

import com.shura.mall.dto.ums.UmsAdminRegisterParam;
import com.shura.mall.model.ums.UmsAdmin;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * @Author: Garvey
 * @Created: 2021/10/18
 * @Description:
 */
public class UmsAdminConverter {

    public static UmsAdmin adminRegisterParamToAdminEntity(UmsAdminRegisterParam adminRegisterParam) {
        UmsAdmin admin = new UmsAdmin();

        admin.setUsername(adminRegisterParam.getUsername());
        admin.setPassword(adminRegisterParam.getPassword());
        admin.setEmail(adminRegisterParam.getEmail());
        admin.setCreateTime(new Date());
        admin.setNickName("");
        admin.setIcon("");
        admin.setNote("");
        admin.setStatus(1);

        if (StringUtils.isNotBlank(adminRegisterParam.getIcon())) {
            admin.setIcon(adminRegisterParam.getIcon());
        }

        if (StringUtils.isNotBlank(adminRegisterParam.getNickName())) {
            admin.setNickName(adminRegisterParam.getNickName());
        }

        if (StringUtils.isNotBlank(adminRegisterParam.getNote())) {
            admin.setNote(adminRegisterParam.getNote());
        }

        return admin;
    }
}
