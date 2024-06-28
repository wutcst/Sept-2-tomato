package com.example.back.sys.token;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 入口拦截配置类，设置哪些接口需要拦截或不拦截（保护后端接口防止未经授权的访问）。
 */
@Configuration
public class IntercepterConfig implements WebMvcConfigurer {
    private final TokenInterceptor tokenInterceptor;

    // 构造方法
    public IntercepterConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //excludePathPatterns用来配置不需要拦截的接口（或者相当于功能）
        List<String> excludePath = new ArrayList<>();//List用来保存所有不需要拦截的路径
        excludePath.add("/player/register"); //注册
        excludePath.add("/player/login"); //登录
        excludePath.add("/player/reset-password"); //修改用户密码

        registry.addInterceptor(tokenInterceptor)//添加名为tokenInterceptor的拦截器
                .addPathPatterns("/**") //指定拦截所有路径
                .excludePathPatterns(excludePath);//排除不需要拦截的路径
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}