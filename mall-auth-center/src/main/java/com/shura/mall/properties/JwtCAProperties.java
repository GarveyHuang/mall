package com.shura.mall.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Garvey
 * @Created: 2021/10/20
 * @Description: jwt 证书配置
 */
@ConfigurationProperties(prefix = "shura.mall.jwt")
public class JwtCAProperties {

    /**
     * 证书名称
     */
    private String keyPairName;

    /**
     * 证书别名
     */
    private String keyPairAlias;

    /**
     * 证书私钥
     */
    private String keyPairSecret;

    /**
     * 证书存储密钥
     */
    private String keyPairStoreSecret;

    public String getKeyPairName() {
        return keyPairName;
    }

    public void setKeyPairName(String keyPairName) {
        this.keyPairName = keyPairName;
    }

    public String getKeyPairAlias() {
        return keyPairAlias;
    }

    public void setKeyPairAlias(String keyPairAlias) {
        this.keyPairAlias = keyPairAlias;
    }

    public String getKeyPairSecret() {
        return keyPairSecret;
    }

    public void setKeyPairSecret(String keyPairSecret) {
        this.keyPairSecret = keyPairSecret;
    }

    public String getKeyPairStoreSecret() {
        return keyPairStoreSecret;
    }

    public void setKeyPairStoreSecret(String keyPairStoreSecret) {
        this.keyPairStoreSecret = keyPairStoreSecret;
    }
}
