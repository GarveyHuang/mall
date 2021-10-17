package com.shura.mall.clientapi.interceptor.config;

import com.shura.mall.clientapi.interceptor.HeaderInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: Feign 配置
 */
public class FeignConfig {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new HeaderInterceptor();
    }
}
