package com.shura.mall.common.exception;

/**
 * @author: Garvey
 * @date: 2021/10/10 10:16 下午
 * @description: 业务异常封装
 */
public class BusinessException extends Exception {

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }
}
