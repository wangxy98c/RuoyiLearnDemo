package com.example.ratelimiter.enums;

/*限流的类型*/
public enum LimitType {
    DEFAULT,//默认的限流策略（针对某个接口限流）
    IP//针对某个IP进行限流
}
