package com.example.ratelimiter.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(RateLimitException.class)
    public Map<String ,Object> retaLimitException(RateLimitException e){
        Map<String,Object> map = new HashMap<>();
        map.put("status",500);
        map.put("message",e.getMessage());
        return map;
    }
}
