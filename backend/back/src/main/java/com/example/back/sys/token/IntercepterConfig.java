package com.example.back.sys.token;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

//入口拦截，设置哪些接口需要拦截或不拦截（保护后端接口 防止未经授权的访问）
//只需要拦截本身就不会携带token的接口（例如登陆注册）
//因为登陆后网页的请求头就会携带token，此时我们需要进行token的验证，防止在未登陆时就可以进行其他操作
@Configuration
public class IntercepterConfig implements WebMvcConfigurer {
    private final TokenInterceptor tokenInterceptor;

    //    构造方法
    public IntercepterConfig(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //excludePathPatterns用来配置不需要拦截的接口（或者相当于功能）
        List<String> excludePath = new ArrayList<>();//List用来保存所有不需要拦截的路径
        excludePath.add("/player/register"); //注册
        excludePath.add("/player/login"); //登录
        excludePath.add("/player/"); //获取所有用户
        excludePath.add("/player/getPlayerInfo"); //获取单个用户
        excludePath.add("/player/reset-password"); //修改用户密码
        excludePath.add("/item/getItemInfo");
        excludePath.add("/item/take");
        excludePath.add("/item/drop");
        excludePath.add("/room/go");
        excludePath.add("/room/back");

        //在登陆之后的网页中已经携带token，所以只需要放行登陆注册接口，
        //若放行其他接口，那么就相当于不需要登陆就可进行接口的使用

        registry.addInterceptor(tokenInterceptor)//添加名为tokenInterceptor的拦截器
                .addPathPatterns("/**") //指定拦截所有路径
                .excludePathPatterns(excludePath);//排除不需要拦截的路径
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}