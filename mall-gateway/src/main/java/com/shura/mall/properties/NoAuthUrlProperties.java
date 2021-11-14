package com.shura.mall.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashSet;

/**
 * @Author: Garvey
 * @Created: 2021/10/15
 * @Description: 配置类 - 经网关访问，配置跳过认证的 url 列表
 */
@Data
@ConfigurationProperties("shura.gateway")
public class NoAuthUrlProperties {

    private LinkedHashSet<String> shouldSkipUrls;
}
