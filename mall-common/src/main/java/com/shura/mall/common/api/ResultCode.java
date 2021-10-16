package com.shura.mall.common.api;

/**
 * @author: Garvey
 * @date: 2021/10/10 9:40 下午
 * @description: 常用 API 操作码枚举
 */
public enum ResultCode implements IErrorCode {

    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(401, "参数校验失败"),
    UNAUTHORIZED(402, "暂未登录或 token 已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    AUTHORIZATION_HEADER_IS_EMPTY(600, "请求头中的 token 为空"),
    GET_TOKEN_KEY_ERROR(601, "获取 token_key 异常"),
    GEN_PUBLIC_KEY_ERROR(602, "生成公钥异常"),
    JWT_TOKEN_VALIDATE_ERROR(603, "token 校验异常");

    private long code;
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
