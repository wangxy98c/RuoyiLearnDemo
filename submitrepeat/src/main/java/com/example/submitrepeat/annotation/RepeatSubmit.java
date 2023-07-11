package com.example.submitrepeat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {
    int intervalue() default 5000;//间隔时间，默认5000ms
    String message() default "不允许重复提交，请稍后再试";//可配置返回的文本
}
