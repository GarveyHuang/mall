package com.shura.mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: Garvey
 * @Created: 2021/11/11
 * @Description:
 */
@Configuration
public class CorsConfig {

    /**
     * 允许跨域请求
     */
    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许 cookie 跨域
        corsConfiguration.setAllowCredentials(true);
        // 允许向该服务器提交请求时的 URI，* 表示全部允许，在 SpringMVC 中，如果设置成 *，会自动转成当前请求头中的 Origin
        corsConfiguration.addAllowedOrigin("*");
        // 允许访问的请求头信息，* 表示全部
        corsConfiguration.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间内，对于相同的跨域请求不会再预检
        corsConfiguration.setMaxAge(18000L);
        // 允许提交请求的方法，* 表示全部允许
        corsConfiguration.addAllowedMethod("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
