package com.shura.mall.common.exception;

import com.shura.mall.common.api.ErrorCode;

/**
 * @author: Garvey
 * @date: 2021/10/10 10:17 下午
 * @description: 网关异常封装
 */
public class GatewayException extends RuntimeException {

    private long code;
    private String message;

    public GatewayException(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
