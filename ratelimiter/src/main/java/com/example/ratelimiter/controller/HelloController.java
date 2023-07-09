package com.example.ratelimiter.controller;

import com.example.ratelimiter.annotation.RateLimiter;
import com.example.ratelimiter.enums.LimitType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    @RateLimiter(time=10,count = 3,limitType = LimitType.IP)//使用自己定义的注解.5秒内3次
    public String hello(){
        return "hello";
    }

}
