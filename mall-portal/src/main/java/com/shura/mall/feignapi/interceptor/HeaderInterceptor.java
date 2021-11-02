package com.shura.mall.feignapi.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Garvey
 * @Created: 2021/11/1
 * @Description: Feign 调用添加请求头
 */
@Slf4j
public class HeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("从 request 中解析请求头：{}", request.getHeader("memberId"));
        template.header("memberId", request.getHeader("memberId"));
    }
}
