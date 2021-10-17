package com.shura.mall.config;

import com.shura.mall.interceptor.BloomFilterInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 拦截器配置
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        registry.addInterceptor(authInterceptorHandler())
                .addPathPatterns("/pms/productInfo/**");
    }

    @Bean
    public BloomFilterInterceptor authInterceptorHandler() {
        return new BloomFilterInterceptor();
    }
}
