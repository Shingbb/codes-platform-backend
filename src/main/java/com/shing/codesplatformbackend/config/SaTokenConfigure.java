package com.shing.codesplatformbackend.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token 拦截器配置类
 *
 * @author Shing
 * date 4/8/2025
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 登录拦截器，默认拦截所有接口
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 用户模块放行
                        "/user/**",
                        // 健康检查接口放行
                        "/health/**",
                        // knife4j文档地址
                        "/doc.html",
                        // swagger-ui
                        "/swagger-ui/**",
                        // webjars
                        "/webjars/**",
                        // api
                        "/v3/api-docs/**",
                        "/error"
                );
    }

}
