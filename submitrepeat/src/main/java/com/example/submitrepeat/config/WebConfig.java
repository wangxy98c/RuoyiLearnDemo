package com.example.submitrepeat.config;

import com.example.submitrepeat.filter.RepeatAbleRequestFilter;
import com.example.submitrepeat.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    RepeatSubmitInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**");//PathPatterns
    }
    @Bean
    FilterRegistrationBean<RepeatAbleRequestFilter> registrationBean(){
        FilterRegistrationBean<RepeatAbleRequestFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RepeatAbleRequestFilter());
        bean.addUrlPatterns("/*");//拦截所有请求 。。注意！！！urlPatterns不是 /**
        return bean;
    }
}
