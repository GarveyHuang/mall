package com.shura.mall.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.api.TokenInfo;
import com.shura.mall.constant.MemberServiceConstant;
import com.shura.mall.mapper.UmsMemberLevelMapper;
import com.shura.mall.mapper.UmsMemberMapper;
import com.shura.mall.model.ums.UmsMember;
import com.shura.mall.model.ums.UmsMemberExample;
import com.shura.mall.model.ums.UmsMemberLevel;
import com.shura.mall.model.ums.UmsMemberLevelExample;
import com.shura.mall.service.IRedisService;
import com.shura.mall.service.IUmsMemberService;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: 会员管理 Service 的实现
 */
@Service
public class UmsMemberServiceImpl implements IUmsMemberService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UmsMemberServiceImpl.class);

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;

    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private UmsMemberMapper memberMapper;
    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;
    @Autowired
    private IRedisService redisService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UmsMember getByUsername(String username) {
        UmsMemberExample example = new UmsMemberExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsMember> memberList = memberMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(memberList)) {
            return memberList.get(0);
        }

        return null;
    }

    @Override
    public UmsMember getById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public CommonResult register(String username, String password, String telephone, String authCode) {
        // 验证验证码
        if (!verifyAuthCode(authCode, telephone)) {
            return CommonResult.failed("验证码错误！");
        }

        // TODO 手机号码入库加密待处理

        // 查询用户是否已存在
        UmsMemberExample memberExample = new UmsMemberExample();
        memberExample.createCriteria().andUsernameEqualTo(username);
        memberExample.or().andPhoneEqualTo(telephone);
        List<UmsMember> memberList = memberMapper.selectByExample(memberExample);
        if (!CollectionUtils.isEmpty(memberList)) {
            return CommonResult.failed("用户名或手机号已存在注册用户！");
        }

        // 用户尚未注册，进行添加用户操作
        UmsMember umsMember = new UmsMember();
        umsMember.setUsername(username);
        umsMember.setPassword(passwordEncoder.encode(password));
        umsMember.setPhone(telephone);

        // 获取会员默认等级并设置
        UmsMemberLevelExample levelExample = new UmsMemberLevelExample();
        levelExample.createCriteria().andDefaultStatusEqualTo(1);
        List<UmsMemberLevel> memberLevelList = memberLevelMapper.selectByExample(levelExample);
        if (!CollectionUtils.isEmpty(memberLevelList)) {
            umsMember.setMemberLevelId(memberLevelList.get(0).getId());
        }
        memberMapper.insert(umsMember);
        umsMember.setPassword(null);
        return CommonResult.success(null,"注册成功");
    }

    @Override
    public CommonResult generateAuthCode(String telephone) {
        String authCode = RandomUtil.randomNumbers(6);

        //验证码绑定手机号并存储到redis
        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE + telephone, authCode);
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE + telephone, AUTH_CODE_EXPIRE_SECONDS);
        return CommonResult.success(authCode,"获取验证码成功");
    }

    @Override
    public CommonResult updatePassword(String telephone, String password, String authCode) {
        // 校验验证码
        if (!verifyAuthCode(authCode, telephone)) {
            return CommonResult.failed("验证码错误！");
        }

        UmsMemberExample memberExample = new UmsMemberExample();
        memberExample.createCriteria().andPhoneEqualTo(telephone);
        List<UmsMember> memberList = memberMapper.selectByExample(memberExample);
        if (CollectionUtils.isEmpty(memberList)) {
            return CommonResult.failed("该账号不存在！");
        }

        UmsMember umsMember = memberList.get(0);
        umsMember.setPassword(passwordEncoder.encode(password));
        memberMapper.updateByPrimaryKeySelective(umsMember);
        return CommonResult.success(null,"密码修改成功");
    }

    @Override
    public UmsMember getCurrentMember() {
        return null;
    }

    @Override
    public void updateIntegration(Long id, Integer integration) {
        UmsMember member = new UmsMember();
        member.setId(id);
        member.setIntegration(integration);
        memberMapper.updateByPrimaryKeySelective(member);
    }

    @Override
    public TokenInfo login(String username, String password) {
        ResponseEntity<TokenInfo> response;
        try {
            // 远程调用认证服务器，进行用户登录
            response = restTemplate.exchange(MemberServiceConstant.OAUTH_LOGIN_URL, HttpMethod.POST, wrapOauthTokenRequest(username, password), TokenInfo.class);
            TokenInfo tokenInfo = response.getBody();
            LOGGER.info("根据用户名：{} 登录成功，TokenInfo：{}", username, tokenInfo);
            return tokenInfo;
        } catch (Exception e) {
            LOGGER.error("根据用户名：{} 登录异常：{}", username, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String refreshToken(String token) {
        LOGGER.info("RefreshToken 的值为：{}", token);

        if (StringUtils.isEmpty(token)) {
            LOGGER.warn("旧令牌为空：{}", token);
            return null;
        }

        ResponseEntity<TokenInfo> responseEntity = null;
        String jwtTokenValue = null;
        try {
            jwtTokenValue = token.substring(tokenHead.length());

            // 刷新令牌
            responseEntity = restTemplate.exchange(MemberServiceConstant.OAUTH_LOGIN_URL, HttpMethod.POST, wrapRefreshTokenRequest(jwtTokenValue), TokenInfo.class);
            TokenInfo tokenInfo = responseEntity.getBody();
            String newAccessToken = tokenInfo.getAccess_token();
            LOGGER.info("通过旧令牌：{}，刷新令牌成功，新令牌：{}", jwtTokenValue, newAccessToken);
            return newAccessToken;
        } catch (Exception e) {
            LOGGER.error("通过旧令牌：{}，刷新 token 失败：{}", jwtTokenValue, e.getMessage());
            return jwtTokenValue;
        }
    }

    @Override
    public int updateUmsMember(UmsMember umsMember) {
        return 0;
    }

    /**
     * 校验验证码
     * @param authCode
     * @param telephone
     * @return
     */
    private boolean verifyAuthCode(String authCode, String telephone) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }

        String realAuthCode = redisService.get(REDIS_KEY_PREFIX_AUTH_CODE + telephone);
        return Objects.equals(authCode, realAuthCode);
    }

    /**
     * 封装用户到认证中心的请求头和请求参数
     * @param username 用户名
     * @param password 密码
     * @return
     */
    private HttpEntity<MultiValueMap<String, String>> wrapOauthTokenRequest(String username, String password) {
        // 封装 oauth2 请求 clientId、clientSecret
        HttpHeaders httpHeaders = wrapHttpHeader();

        // 封装请求参数
        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        reqParams.add(MemberServiceConstant.USERNAME, username);
        reqParams.add(MemberServiceConstant.PASS, password);
        reqParams.add(MemberServiceConstant.GRANT_TYPE, MemberServiceConstant.PASS);
        reqParams.add(MemberServiceConstant.SCOPE, MemberServiceConstant.SCOPE_AUTH);

        return new HttpEntity<>(reqParams, httpHeaders);
    }

    /**
     * 封装刷新 token 的请求
     * @param refreshToken 要刷新的 token
     * @return
     */
    private HttpEntity<MultiValueMap<String, String>> wrapRefreshTokenRequest(String refreshToken) {
        HttpHeaders httpHeaders = wrapHttpHeader();

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("grant_type", "refresh_token");
        param.add("refresh_token", refreshToken);

        return new HttpEntity<>(param, httpHeaders);
    }

    /**
     * 封装请求头
     * @return
     */
    private HttpHeaders wrapHttpHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(MemberServiceConstant.CLIENT_ID, MemberServiceConstant.CLIENT_SECRET);
        return httpHeaders;
    }
}
