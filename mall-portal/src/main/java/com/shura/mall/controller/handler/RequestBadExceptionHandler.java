package com.shura.mall.controller.handler;

import com.shura.mall.common.api.CommonResult;
import com.shura.mall.common.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description: 全局异常处理，避免异常导致敏感信息直接暴露给客户端
 */
@ControllerAdvice
public class RequestBadExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public CommonResult<String> exceptionHandler(Exception e) {
        return CommonResult.failed("请求错误：-> " + e.getMessage());
    }
}
