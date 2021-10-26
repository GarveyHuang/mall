package com.shura.mall.service.base.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.shura.mall.domain.base.OssCallbackParam;
import com.shura.mall.domain.base.OssCallbackResult;
import com.shura.mall.domain.base.OssPolicyResult;
import com.shura.mall.service.base.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: OSS 文件上传管理 Service 实现类
 */
@Slf4j
@Service("ossService")
public class OssServiceImpl implements OssService {

    @Value("${aliyun.oss.policy.expire}")
    private int ALIYUN_OSS_EXPIRE;
    @Value("${aliyun.oss.maxSize}")
    private int ALIYUN_OSS_MAX_SIZE;
    @Value("${aliyun.oss.callback}")
    private String ALIYUN_OSS_CALLBACK;
    @Value("${aliyun.oss.bucketName}")
    private String ALIYUN_OSS_BUCKET_NAME;
    @Value("${aliyun.oss.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;
    @Value("${aliyun.oss.dir.prefix}")
    private String ALIYUN_OSS_DIR_PREFIX;

    @Autowired
    private OSSClient ossClient;

    /**
     * 签名生成
     * @return
     */
    @Override
    public OssPolicyResult policy() {
        OssPolicyResult result = new OssPolicyResult();
        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = ALIYUN_OSS_DIR_PREFIX + sdf.format(new Date());

        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + ALIYUN_OSS_EXPIRE * 1000L;
        Date expiration = new Date(expireEndTime);

        // 文件大小
        long maxSize = ALIYUN_OSS_MAX_SIZE * 1024L * 1024L;

        // 回调
        OssCallbackParam callback = new OssCallbackParam();
        callback.setCallbackUrl(ALIYUN_OSS_CALLBACK);
        callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCallbackBodyType("application/x-www-form-urlencoded");

        // 提交节点
        String action = "http://" + ALIYUN_OSS_BUCKET_NAME + "." + ALIYUN_OSS_ENDPOINT;
        try {
            PolicyConditions conditions = new PolicyConditions();
            conditions.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            conditions.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, conditions);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(callback).toString().getBytes(StandardCharsets.UTF_8));

            // 返回结果
            result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setCallback(callbackData);
            result.setHost(action);
        } catch (Exception e) {
            log.error("签名生成失败", e);
        }

        return result;
    }

    @Override
    public OssCallbackResult callback(HttpServletRequest request) {
        OssCallbackResult result = new OssCallbackResult();
        String filename = request.getParameter("filename");
        filename = "http://"
                .concat(ALIYUN_OSS_BUCKET_NAME).concat(".")
                .concat(ALIYUN_OSS_ENDPOINT).concat("/")
                .concat(filename);
        result.setFilename(filename);
        result.setSize(request.getParameter("size"));
        result.setMimeType(request.getParameter("mimeType"));
        result.setHeight(request.getParameter("height"));
        result.setWidth(request.getParameter("width"));
        return result;
    }
}
