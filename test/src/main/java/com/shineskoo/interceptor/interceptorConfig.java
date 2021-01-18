package com.shineskoo.interceptor;

/*
 *   @Author: Cosmos
 *   @Date: 2021/1/13 4:56 下午
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器的配置类
 */
@Configuration
public class interceptorConfig implements WebMvcConfigurer {
    @Bean
    public IdentityCheckInterceptor getCheckInterceptor() {
        return new IdentityCheckInterceptor();
    }

    /**
     * 注册拦截器，添加拦截路径或排除拦截路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getCheckInterceptor()).addPathPatterns("/**");
    }
}
