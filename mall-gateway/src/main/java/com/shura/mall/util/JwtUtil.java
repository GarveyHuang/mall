package com.shura.mall.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.shura.mall.common.api.ResultCode;
import com.shura.mall.common.exception.GatewayException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/11/15
 * @Description: JWT 操作工具类
 */
@Slf4j
public class JwtUtil {

    /**
     * 认证服务器许可网关的 clientId（需要在 oauth_client_details 表中配置）
     */
    private static final String CLIENT_ID = "api-gateway";

    /**
     * 认证服务器许可网关的 client_secret（需要在 oauth_client_details 表中配置）
     */
    private static final String CLIENT_SECRET = "shura-gateway";

    /**
     * 认证服务器暴露的获取 token_key 地址
     */
    private static final String AUTH_TOKEN_KEY_URL = "http://shura-authcenter/oauth/token_key";

    /**
     * 请求头中 token 前缀起始字符
     */
    private static final String AUTH_HEADER = "bearer ";

    /**
     * 生成公钥
     * @param restTemplate 远程调用操作类
     * @return
     * @throws GatewayException
     */
    public static PublicKey getPublicKey(RestTemplate restTemplate) throws GatewayException {
        String tokenKey = getTokenKey(restTemplate);

        try {
            // 把获取到的公钥开头和结尾替换掉
            String dealTokenKey = tokenKey.replaceAll("-*BEGIN PUBLIC KEY-*", "")
                    .replaceAll("-*END PUBLIC KEY-*", "").trim();

            java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decode(dealTokenKey));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(pubKeySpec);
            log.info("生成公钥：{}", publicKey);

            return publicKey;
        } catch (Exception e) {
            log.error("生成公钥异常：{}", e.getMessage());
            throw new GatewayException(ResultCode.GEN_PUBLIC_KEY_ERROR);
        }
    }

    public static Claims validateJwtToken(String authHeader, PublicKey publicKey) {
        String token = null;
        try {
            token = StringUtils.substringAfter(authHeader, AUTH_HEADER);
            Jwt<JwsHeader, Claims> parseClaimsJwt = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
            Claims claims = parseClaimsJwt.getBody();
            log.info("claims：{}", claims);
            return claims;
        } catch (Exception e) {
            log.error("校验 token 异常：{}，异常信息：{}", token, e.getMessage());
            throw new GatewayException(ResultCode.JWT_TOKEN_VALIDATE_ERROR);
        }
    }

    private static String getTokenKey(RestTemplate restTemplate) throws GatewayException {
        // 1. 封装请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);

        // 2. 远程调用获取 token_key
        try {
            ResponseEntity<Map> response = restTemplate.exchange(AUTH_TOKEN_KEY_URL, HttpMethod.GET, entity, Map.class);
            String tokenKey = response.getBody().get("value").toString();
            log.info("远程调用认证服务器获取 token_key：{}", tokenKey);
            return tokenKey;
        } catch (Exception e) {
            log.error("远程调用认证服务器获取 token_key 失败：{}", e.getMessage());
            throw new GatewayException(ResultCode.GET_TOKEN_KEY_ERROR);
        }
    }
}
