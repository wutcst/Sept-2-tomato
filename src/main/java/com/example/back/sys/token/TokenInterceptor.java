package com.example.back.sys.token;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//token拦截器,对拦截下的接口检查其的token是否符合只有
// 在提供一个有效的token时才能通过验证,否则给出认证失败的响应。
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //Axios 发起跨域请求前,浏览器也会首先发起 OPTIONS 预检请求。检查服务器是否允许跨域访问。
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            System.out.println("允许跨域访问");
            return true;
        }

        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        if (token != null) {
            boolean result = TokenGenerate.verify(token);
            if (result) {
                System.out.println("通过拦截器");
                return true;
            }
        }
        response.setCharacterEncoding("UTF-8");
        System.out.println("认证失败，token错误或不存在");
        response.getWriter().write("认证失败,token不存在");
        return false;
    }
}