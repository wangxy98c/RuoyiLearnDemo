package com.example.ratelimiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class RedisConfig {
    //Redis提供的两个可以操作Redis的类
    //RedisTemplate redisTemplate;
    //可以操作对象(key\value可以是对象).但它使用的序列化方式是JDK里的，会有一堆前缀。导致命令行（或相当于命令行）操作会出问题。需要定制序列化方案
    //StringRedisTemplate stringRedisTemplate;//必须是字串
    @Bean//定制序列化方案
    RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return template;
    }
    @Bean
    DefaultRedisScript<Long> limitScript(){
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        //配置脚本返回类型以及脚本位置
        script.setResultType(Long.class);
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/limit.lua")));
        return script;
    }
}
