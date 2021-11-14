package com.shura.mall.config;

import com.shura.mall.component.MyRestTemplate;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garvey
 * @Created: 2021/11/14
 * @Description:
 */
@Configuration
public class RibbonConfig {

    /**
     * 原生的 RestTemplate +@LB 不行，
     * 因为在 InitializingBean 方法执行前 RestTemplate 还没有被增强，
     * 所以需要自己改写 RestTemplate
     */
    @Bean
    public MyRestTemplate restTemplate(DiscoveryClient discoveryClient) {
        return new MyRestTemplate(discoveryClient);
    }
}
