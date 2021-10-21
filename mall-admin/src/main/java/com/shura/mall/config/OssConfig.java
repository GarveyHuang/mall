package com.shura.mall.config;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description:
 */
@Configuration
public class OssConfig {

    @Value("${aliyun.oss.endpoint}")
    private String ALIYUN_OSS_ENDPOINT;

    @Value("${aliyun.oss.accessKeyId}")
    private String ALIYUN_OSS_ACCESS_KEY_ID;

    @Value("${aliyun.oss.accessKeySecret}")
    private String ALIYUN_OSS_ACCESS_KEY_SECRET;

    @Bean
    public OSSClient ossClient() {
        return new OSSClient(ALIYUN_OSS_ENDPOINT, ALIYUN_OSS_ACCESS_KEY_ID, ALIYUN_OSS_ACCESS_KEY_SECRET);
    }
}
