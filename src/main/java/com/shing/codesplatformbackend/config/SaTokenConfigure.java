package com.shing.codesplatformbackend.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Sa-Token 拦截器配置类,注册全局过滤器
 *
 * @author Shing
 * date 4/8/2025
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 定义无需登录验证的路径
    private static final List<String> EXCLUDE_PATH_PATTERNS = Arrays.asList(
            "/health/**",
            "/user/login",
            "/user/register",
            "/doc.html",
            "/swagger-ui/**",
            "/webjars/**",
            "/v3/api-docs/**",
            "/error/*"
    );

    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 登录拦截器，默认拦截所有接口
        registry.addInterceptor(new SaInterceptor(handle -> {
                    // 排除无需登录验证的路径
//                    SaRouter.match(EXCLUDE_PATH_PATTERNS).free(r -> System.out.println("跳过认证路径: " + r));

                    // 仅进行登录校验，角色权限控制由 @SaCheckRole 注解处理
//                    StpUtil.checkLogin();
                }))
                .addPathPatterns("/**");
    }

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")
                .addExclude(EXCLUDE_PATH_PATTERNS.toArray(new String[0])) // 统一排除路径配置
                .setAuth(obj -> {
                    // 排除无需登录验证的路径
                    SaRouter.match(EXCLUDE_PATH_PATTERNS).free(r -> System.out.println("跳过认证路径: " + r));

                    // 进行登录校验
                    StpUtil.checkLogin();
                })
                .setError(e -> {
                    // 异常处理
                    return SaResult.error(e.getMessage());
                });
    }
}