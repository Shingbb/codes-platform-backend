package com.shing.codesplatformbackend.common;


import com.shing.codesplatformbackend.exception.ErrorCode;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *  统一返回结果工具类
 *
 * @author shing
 */
public class ResultUtils {

    /**
     * MessageSource 用于国际化消息解析
     */
    @Setter
    private static MessageSource messageSource;

    // 私有构造，禁止实例化
    private ResultUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 根据 key 获取国际化消息
     *
     * @param key 消息键
     * @return 国际化消息
     */
    private static String getMessage(String key) {
        if (messageSource == null) {
            return key;
        }
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    /**
     * 成功返回
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ErrorCode.SUCCESS.getCode(), data, getMessage(ErrorCode.SUCCESS.getMessageKey()));
    }

    /**
     * 错误返回（用 ErrorCode）
     */
    public static BaseResponse<Void> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), null, getMessage(errorCode.getMessageKey()));
    }

    /**
     * 错误返回（用自定义 code 和 messageKey）
     */
    public static BaseResponse<Void> error(int code, String messageKey) {
        return new BaseResponse<>(code, null, getMessage(messageKey));
    }

    /**
     * 错误返回（用 ErrorCode 和自定义 messageKey）
     */
    public static BaseResponse<Void> error(ErrorCode errorCode, String customMsgKey) {
        return new BaseResponse<>(errorCode.getCode(), null, getMessage(customMsgKey));
    }
}
