package com.example.submitrepeat.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/*
* 注意：
* 由于没有设置序列化的方案，故而Redis的key会带有一些前缀
* 序列化方案参考retelimiter */
@Component
public class RedisCache {
    @Autowired
    RedisTemplate redisTemplate;
    public <T> void setCacheObj(final String key, final T value, Integer timeout, final TimeUnit timeType){
        redisTemplate.opsForValue().set(key,value,timeout,timeType);
    }
    public <T> T getCacheObj(final String key){
        ValueOperations<String,T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}
