package com.shura.mall.common.api;

/**
 * @author: Garvey
 * @date: 2021/10/10 9:40 下午
 * @description: 封装 API 的错误码
 */
public interface IErrorCode {

    long getCode();

    String getMessage();
}
