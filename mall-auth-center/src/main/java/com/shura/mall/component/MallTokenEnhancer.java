package com.shura.mall.component;

import com.shura.mall.domain.MemberDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Garvey
 * @Created: 2021/10/20
 * @Description: jwt 自定义增强器（根据业务需求添加非敏感字段）
 */
public class MallTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();
        final Map<String, Object> retMap = new HashMap<>();

        // TODO 这里暴露 memberId 到 jwt 令牌中，后期可以根据业务需要进行字段添加
        additionalInfo.put("memberId", memberDetails.getUsername());
        additionalInfo.put("nickname", memberDetails.getUmsMember().getNickname());
        additionalInfo.put("integration", memberDetails.getUmsMember().getIntegration());

        retMap.put("additionalInfo", additionalInfo);

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(retMap);
        return accessToken;
    }
}
