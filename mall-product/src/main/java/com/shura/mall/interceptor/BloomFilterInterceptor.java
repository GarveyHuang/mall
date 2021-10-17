package com.shura.mall.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.constant.RedisKeyPrefixConst;
import com.shura.mall.component.BloomRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/10/17
 * @Description: 拦截器
 */
@Slf4j
public class BloomFilterInterceptor implements HandlerInterceptor {

    @Autowired
    private BloomRedisService bloomRedisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentUrl = request.getRequestURI();
        PathMatcher matcher = new AntPathMatcher();
        // 解析出 pathVariable
        Map<String, String> pathVariable = matcher.extractUriTemplateVariables("/pms/productInfo/{id}", currentUrl);
        // 布隆过滤器存储在 Redis 中
        if (bloomRedisService.includeByBloomFilter(RedisKeyPrefixConst.PRODUCT_REDIS_BLOOM_FILTER, pathVariable.get("id"))) {
            return true;
        }

        // 存储在本地布隆过滤器中
//        if (LocalBloomFilter.match(pathVariable.get("id"))) {
//            return true;
//        }

        // 设置响应头
        response.setHeader("Content-Type", "application/json");
        request.setCharacterEncoding("UTF-8");

        String result = new ObjectMapper().writeValueAsString(CommonResult.validateFailed("商品不存在！"));
        response.getWriter().println(result);
        return false;
    }
}
