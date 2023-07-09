package com.example.ratelimiter.aspectj;

import com.example.ratelimiter.annotation.RateLimiter;
import com.example.ratelimiter.enums.LimitType;
import com.example.ratelimiter.exception.RateLimitException;
import com.example.ratelimiter.utils.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Collections;

@Aspect
@Component
public class RateLimitAspect {
    private static  final Logger logger= LoggerFactory.getLogger(RateLimitAspect.class);
    @Autowired
    RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    RedisScript<Long> redisScript;
    @Before("@annotation(rateLimiter)")//此处没有使用PointCut而直接使用@Before等注解
    public void before(JoinPoint jp, RateLimiter rateLimiter){
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey=getCombineKey(rateLimiter,jp);
        //调用LUA，由于三个参数都是数组，需要把key转化成数组的一项

        try {
            Long aLong = redisTemplate.execute(redisScript, Collections.singletonList(combineKey), time, count);
            if(aLong==null || aLong.intValue()>count){//超过限流
                logger.info("超过限流阈值");
                throw new RateLimitException("访问过于频繁，超出单位时间内请求次数阈值");
            }
            //没问题，什么都不用做（不需要Around pjp的继续）
            logger.info("大小为{}的窗口内请求次数为{},key值为{}",count,aLong,combineKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private String getCombineKey(RateLimiter rateLimiter, JoinPoint jp) {
        /*就是Redis的key 。和ip-method 有关。
        * redis-key的格式,拼接成如下形式：后两个字段是类和方法，第一个字段是ip(可剔除不限ip)
        * 11.11.11.11-org.javaboy.ratelimit.controller.HelloController-hello  */
        StringBuffer key = new StringBuffer(rateLimiter.key());//有默认值"rate_limit"
        if(rateLimiter.limitType()== LimitType.IP){//如果是基于Ip地址限流，就需要把ip拼接上去
            //获得ip地址（从网上找一个工具类）.从RequestContextHolder中拿到请求的相关信息
            //System.out.println(IpUtils.getIpAddr(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()));
            key.append(IpUtils.getIpAddr(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()))
                    .append("-");//有一点小问题，输出的ip地址是0:0:0:0:0:0:0:1。先不管了
        }
        MethodSignature signature = (MethodSignature) jp.getSignature();//切点处
        Method method = signature.getMethod();//拿到切点方法的相关信息
        key.append(method.getDeclaringClass().getName())//方法的路径
                .append("-")
                .append(method.getName());
        return key.toString();
    }
}
