package com.shura.mall.filter;

import cn.hutool.json.JSONUtil;
import com.shura.mall.common.api.ResultCode;
import com.shura.mall.common.exception.GatewayException;
import com.shura.mall.component.MyRestTemplate;
import com.shura.mall.properties.NoAuthUrlProperties;
import com.shura.mall.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.security.PublicKey;
import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/11/16
 * @Description: 认证过滤器，根据请求 url 判断用户请求是否需要经过验证才能访问
 */
@Slf4j
@Component
@EnableConfigurationProperties(value = NoAuthUrlProperties.class)
public class AuthorizationFilter implements GlobalFilter, Ordered, InitializingBean {

    @Resource
    private MyRestTemplate restTemplate;

    /**
     * 请求各个微服务时，不需要用户认证的 URL
     */
    @Autowired
    private NoAuthUrlProperties noAuthUrlProperties;


    /**
     * jwt 的公钥，需要网关启动，远程调用认证中心去获取公钥
     */
    private PublicKey publicKey;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化公钥
        this.publicKey = JwtUtil.getPublicKey(restTemplate);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String currentUrl = exchange.getRequest().getURI().getPath();
        
        // 判断是否不需要认证的 url
        if (shouldSkip(currentUrl)) {
            return chain.filter(exchange);
        }
        
        // 1. 解析 Authorization 请求头，value 为："bearer xxxxxxxx"
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        
        // 2. 判断 Authorization 请求头参数是否合法
        if (StringUtils.isBlank(authHeader)) {
            log.warn("需要认证的 url，请求头为空");
            throw new GatewayException(ResultCode.AUTHORIZATION_HEADER_IS_EMPTY);
        }
        
        // 3. 校验 jwt 是否有效
        Claims claims = JwtUtil.validateJwtToken(authHeader, publicKey);
        
        // 4. 把从 jwt 中解析出来的用户登录信息存储到请求头中
        ServerWebExchange webExchange = wrapHead(exchange, claims);
        
        return chain.filter(webExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 不需要授权 url
     * @param currentUrl
     * @return
     */
    private boolean shouldSkip(String currentUrl) {
        // 路径匹配 - SpringMVC
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String skipPath : noAuthUrlProperties.getShouldSkipUrls()) {
            if (pathMatcher.match(skipPath, currentUrl)) {
                return true;
            }
        }

        return false;
    }
    
    private ServerWebExchange wrapHead(ServerWebExchange exchange, Claims claims) {
        String loginUserInfo = JSONUtil.toJsonStr(claims);
        Map map = claims.get("additionalInfo", Map.class);
        String memberId = map.get("memberId").toString();
        String nickName = map.get("nickname").toString();

        // 向 headers 中放文件
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("username", claims.get("user_name", String.class))
                .header("memberId", memberId)
                .header("nickname", nickName)
                .build();

        // 将 request 转换为 change 对象
        return exchange.mutate().request(request).build();
    }
}
