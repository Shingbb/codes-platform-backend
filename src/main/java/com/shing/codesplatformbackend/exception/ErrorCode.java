package com.shing.codesplatformbackend.exception;

import lombok.Getter;

/**
 * 自定义错误码
 * 错误码范围：
 * 0 - 成功
 * 4XXXX - 客户端错误
 * 5XXXX - 服务器错误
 *
 * @author shing
 */
@Getter
public enum ErrorCode {

    // 成功
    SUCCESS(0, "success.message"),

    // 通用客户端错误
    PARAMS_ERROR(40000, "params.error"),

    INVALID_REQUEST(40001, "invalid.request"),

    METHOD_NOT_ALLOWED(40500, "method.not.allowed"),

    NOT_LOGIN_ERROR(40100, "not.login"),

    NO_AUTH_ERROR(40101, "no.auth"),

    NOT_FOUND_ERROR(40400, "not.found"),

    FORBIDDEN_ERROR(40300, "forbidden"),

    // 通用服务端错误
    SYSTEM_ERROR(50000, "system.error"),

    SERVICE_UNAVAILABLE(50300, "service.unavailable"),

    OPERATION_ERROR(50001, "operation.error");

    /**
     * 错误码
     * 错误码范围：0 - 成功，4XXXX - 客户端错误，5XXXX - 服务器错误
     */
    private final int code;

    /**
     * 错误信息
     * 说明了错误的详细信息，帮助用户或者开发者理解具体的错误原因。
     */
    private final String messageKey;

    /**
     * 构造函数，初始化错误码和错误信息
     *
     * @param code       错误码
     * @param messageKey 错误信息
     */
    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

}
