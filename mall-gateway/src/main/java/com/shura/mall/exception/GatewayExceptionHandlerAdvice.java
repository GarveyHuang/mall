package com.shura.mall.exception;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.exception.GatewayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: Garvey
 * @Created: 2021/11/11
 * @Description: 网关异常处理切面类
 */
@Slf4j
@Component
public class GatewayExceptionHandlerAdvice {

    @ExceptionHandler(value = { GatewayException.class })
    public CommonResult handle(GatewayException ex) {
        log.error("网关处理异常，code：{}，msg：{}", ex.getCode(), ex.getMessage());
        return CommonResult.failed(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(value = { Throwable.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult handle(Throwable throwable) {
        if (throwable instanceof  GatewayException) {
            return handle((GatewayException) throwable);
        }

        return CommonResult.failed();
    }
}
