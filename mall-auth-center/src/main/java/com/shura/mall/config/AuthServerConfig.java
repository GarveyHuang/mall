package com.shura.mall.config;

import com.shura.mall.component.MallTokenEnhancer;
import com.shura.mall.properties.JwtCAProperties;
import com.shura.mall.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.util.Arrays;

/**
 * @Author: Garvey
 * @Created: 2021/10/20
 * @Description: 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(value = JwtCAProperties.class)
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtCAProperties jwtCAProperties;

    /**
     * 颁发的 token 通过 jwt 存储
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // jwt 的密钥
        converter.setKeyPair(keyPair());
        return converter;
    }

    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(jwtCAProperties.getKeyPairName()), jwtCAProperties.getKeyPairSecret().toCharArray());
        return keyStoreKeyFactory.getKeyPair(jwtCAProperties.getKeyPairAlias(), jwtCAProperties.getKeyPairStoreSecret().toCharArray());
    }

    @Bean
    public MallTokenEnhancer mallTokenEnhancer() {
        return new MallTokenEnhancer();
    }

    /**
     * 认证服务器能够给哪些客户端颁发 token，需要把客户端的配置存储到数据库中（可以基于内存和 DB 存储）
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * 用于查找第三方客户端的组件，主要用于查找数据库表 oauth_client_details
     * @return
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 授权服务器相关配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(mallTokenEnhancer(), jwtAccessTokenConverter()));

        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }
}
