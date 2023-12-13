package com.atguigu.config;

import com.atguigu.interceptor.LoginProtectedIntertector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoginProtectedIntertector loginProtectedIntertector;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginProtectedIntertector).addPathPatterns("/headline/**");
    }
}
