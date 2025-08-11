package com.shing.codesplatformbackend.exception;


import lombok.Getter;

/**
 * 自定义异常类
 *
 * @author shing
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    private final String messageKey;

    public BusinessException(int code, String messageKey) {
        super(messageKey);
        this.code = code;
        this.messageKey = messageKey;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessageKey());
        this.code = errorCode.getCode();
        this.messageKey = errorCode.getMessageKey();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
        // 如果传了自定义 message，但还是需要一个 messageKey，方便国际化
        this.messageKey = errorCode.getMessageKey();
    }

}
