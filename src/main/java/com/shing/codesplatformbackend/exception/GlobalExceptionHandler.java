package com.shing.codesplatformbackend.exception;


import com.shing.codesplatformbackend.common.BaseResponse;
import com.shing.codesplatformbackend.common.ResultUtils;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.NotLoginException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author shing
 */
@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Sa-Token 登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<Void> notLoginExceptionHandler(NotLoginException e) {
        log.error("NotLoginException", e);
        return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
    }

    /**
     * Sa-Token 角色权限异常
     */
    @ExceptionHandler(NotRoleException.class)
    public BaseResponse<Void> notRoleExceptionHandler(NotRoleException e) {
        log.error("NotRoleException", e);
        return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Void> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 参数校验异常（@RequestBody + @Validated）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("参数校验失败");
        log.warn("参数校验异常: {}", errorMsg);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, errorMsg);
    }

    /**
     * 参数绑定异常（@RequestParam / @PathVariable + @Validated）
     */
    @ExceptionHandler(BindException.class)
    public BaseResponse<Void> handleBindException(BindException e) {
        String errorMsg = e.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("参数绑定失败");
        log.warn("参数绑定异常: {}", errorMsg);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR, errorMsg);
    }

    /**
     * 运行时异常兜底处理
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Void> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "system.error");
    }
}