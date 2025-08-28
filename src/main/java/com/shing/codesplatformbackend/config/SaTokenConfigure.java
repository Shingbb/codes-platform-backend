package com.shing.codesplatformbackend.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    // 定义无需登录验证的路径
    private static final List<String> EXCLUDE_PATH_PATTERNS = Arrays.asList("/health/**", "/user/login", "/user/register", "/doc.html", "/swagger-ui/**", "/webjars/**", "/v3/api-docs/**", "/error/*");

    // 注册同步拦截器（用于Servlet环境）
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 登录拦截器，默认拦截所有接口
        registry.addInterceptor(new SaInterceptor(handler -> {
                    // 使用 SaRouter 进行路径匹配和认证检查
                    SaRouter
                            // 匹配所有路径
                            .match("/**")
                            // 排除无需认证的路径
                            // 排除无需认证的路径
                            .notMatch(EXCLUDE_PATH_PATTERNS)
                            // 额外排除流式接口路径
                            .notMatch("/app/chat/gen/code").check(r -> {
                                // 检查登录状态
                                StpUtil.checkLogin();
                            });
                })).addPathPatterns("/**")
                // 排除无需认证的路径
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }

    // 注册响应式过滤器（用于WebFlux环境）
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截所有路径
                .addInclude("/**")
                // 排除无需认证的路径
                .addExclude(EXCLUDE_PATH_PATTERNS.toArray(new String[0]))
                // 认证函数
                .setAuth(obj -> {
                    // 输出日志，便于调试
                    log.info("---------- Sa-Token 全局认证 (WebFlux) ----------");
                    log.info("请求路径: {}", SaHolder.getRequest().getRequestPath());

                    // 使用 SaRouter 进行路径匹配和认证检查
                    SaRouter.match("/**") // 匹配所有路径
                            .notMatch(EXCLUDE_PATH_PATTERNS) // 排除无需认证的路径
                            .check(r -> {
                                // 检查登录状态
                                StpUtil.checkLogin();
                                // 可以在这里添加更多权限检查逻辑
                            });
                })
                // 异常处理函数
                .setError(e -> {
                    log.info("---------- Sa-Token 全局异常 (WebFlux) ----------");
                    log.info("异常信息: {}", e.getMessage());
                    return SaResult.error("认证失败: " + e.getMessage());
                });

    }
}