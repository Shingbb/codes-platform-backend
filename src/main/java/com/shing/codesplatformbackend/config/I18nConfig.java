package com.shing.codesplatformbackend.config;

import com.shing.codesplatformbackend.common.ResultUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * 国际化配置
 *
 * @author Shing
 * date 10/8/2025
 */
@Configuration
public class I18nConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
        ms.setBasename("messages"); // 对应 resources/messages.properties
        ms.setDefaultEncoding("UTF-8");
        ms.setUseCodeAsDefaultMessage(true);
        ResultUtils.setMessageSource(ms); // 注入到 ResultUtils
        return ms;
    }

    /**
     * Locale解析器，基于请求头的Accept-Language
     */
    @Bean
    public LocaleResolver localeResolver() {
        // AcceptHeaderLocaleResolver根据请求头中的 Accept-Language 设置Locale
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return resolver;
    }
}
