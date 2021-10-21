package com.shura.mall.service.base;

import com.shura.mall.dto.base.OssCallbackResult;
import com.shura.mall.dto.base.OssPolicyResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: OSS 文件上传管理 Service
 */
public interface IOssService {

    /**
     * OSS 上传策略生成
     */
    OssPolicyResult policy();

    /**
     * Oss 上传成功回调
     */
    OssCallbackResult callback(HttpServletRequest request);
}
