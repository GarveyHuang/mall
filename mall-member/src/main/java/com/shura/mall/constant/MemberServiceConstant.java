package com.shura.mall.constant;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 会员服务常量
 */
public class MemberServiceConstant {

    /**
     * 会员服务第三方客户端（是在认证服务配置好的 oauth_client_details）
     */
    public static final String CLIENT_ID = "member-service";

    /**
     * 会员服务第三方客户端密钥（是在认证服务配置好的 oauth_client_details）
     */
    public static final String CLIENT_SECRET = "shura-member";

    /**
     * 认证服务器登录地址
     */
    public static final String OAUTH_LOGIN_URL = "http://mall-auth-center/oauth/token";

    public static final String  USERNAME = "username";

    public static final String PASS = "password";

    public static final String GRANT_TYPE = "grant_type";

    public static final String SCOPE = "scope";

    public static final String SCOPE_AUTH = "read";
}
