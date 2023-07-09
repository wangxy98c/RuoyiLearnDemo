package com.example.ratelimiter.annotation;

import com.example.ratelimiter.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {
    /*限流的key，主要是指前缀。*/
    String key() default "rate_limit:";
    /*限流时间窗口*/
    int time() default 60;
    /*时间窗口内的限流次数*/
    int count() default 100;
    /*限流的类型*/
    LimitType limitType() default LimitType.DEFAULT;
}
