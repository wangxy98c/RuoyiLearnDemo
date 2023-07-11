package com.example.submitrepeat.interceptor;

import com.example.submitrepeat.annotation.RepeatSubmit;
import com.example.submitrepeat.redis.RedisCache;
import com.example.submitrepeat.request.RepeatAbleReadRequestWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RepeatSubmitInterceptor implements HandlerInterceptor {
    @Autowired
    RedisCache redisCache;
    public static final String REPEAT_PARAMS="repeat_params";//作为map的key
    public static final String REPEAT_TIME="repeat_time";
    public static final String HEADER="Authorization";
    public static final String REPEAT_SUBMIT_KEY="repeat_submit_key";



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //ystem.out.println("line:"+request.getReader().readLine());//此处读了，controller中再读(return json)会出错（JSON形式会，Key-Value不会）
        if(handler instanceof HandlerMethod){
            //每个接口方法（如Controller）都封装成了一个类，这个类/对象就是HandlerMethod。
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
            if(repeatSubmit!=null){
                //说明有@RepeatSubmit注解,需要处理
                if(isRepeatSubmit(request,repeatSubmit)){
                    //是重复提交
                    Map<String, Object> map = new HashMap<>();
                    map.put("status",500);
                    map.put("message",repeatSubmit.message());
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(map));
                    return false;
                }
            }
        }
        return true;
    }

    //判断是否是重复的提交，Redis
    private boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit) {
        String nowParams="";
        if(request instanceof RepeatAbleReadRequestWrapper){
            //说明是一个JSON请求，读取出来请求信息
            try {
                nowParams= ((RepeatAbleReadRequestWrapper) request).getReader().readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(StringUtil.isNullOrEmpty(nowParams)){//说明是key-value形式的请求
            try {
                nowParams=new ObjectMapper().writeValueAsString(request.getParameterMap());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Params ========>"+nowParams);//就是请求体RequestBody的内容
        Map<String,Object> nowDataMap=new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS,nowParams);
        nowDataMap.put(REPEAT_TIME,System.currentTimeMillis());
        //拼接Reids的key
        String requestURI=request.getRequestURI();
        String header = request.getHeader(HEADER);//其实就是拿到请求的Authorization令牌
        String cacheKey=REPEAT_SUBMIT_KEY+requestURI+header.replace("Bearer","");//拼接成Redis的key
        Object cacheObj = redisCache.getCacheObj(cacheKey);//先看看最近请求过没
        if(cacheObj!=null){
            Map<String,Object> map = (Map<String,Object>) cacheObj;
            if(compareParams(map,nowDataMap) && compareTime(map,nowDataMap,repeatSubmit.intervalue())){
                //比较map(里的Params，不包括时间），以及时间是否过短。间隔很短且数据相同自然就是重复提交
                return true;
            }
        }
        //没请求过，把它记录到Redis里
        redisCache.setCacheObj(cacheKey,nowDataMap,repeatSubmit.intervalue(), TimeUnit.MILLISECONDS);
        return false;
    }

    private boolean compareTime(Map<String, Object> map, Map<String, Object> nowDataMap, int intervalue) {
        Long time1 = (Long) map.get(REPEAT_TIME);//redis中取出来的
        Long time2 = (Long) nowDataMap.get(REPEAT_TIME);
        if((time2-time1)<intervalue){
            return true;
        }
        return false;
    }

    private boolean compareParams(Map<String, Object> map, Map<String, Object> nowDataMap) {
        String nowParams= (String) nowDataMap.get(REPEAT_PARAMS);
        String dataParams=(String) map.get(REPEAT_PARAMS);
        return nowParams.equals(dataParams);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
