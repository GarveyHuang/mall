package com.shura.mall.common.api;

import java.util.Map;

/**
 * @author: Garvey
 * @date: 2021/10/10 10:12 下午
 * @description: 认证服务器返回的 TokenInfo 的封装
 */
public class TokenInfo {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private String scope;

    private Map<String, String> additionalInfo;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(Map<String, String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
