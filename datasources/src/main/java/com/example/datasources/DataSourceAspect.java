package com.example.datasources;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/*自定义一个切面（AOP依赖），当有@DataSource注解时，把数据源存入*/
@Component
@Aspect
@Order(10) //为了优先级低于网页修改数据源.
public class DataSourceAspect {
    /*@annotation(com.example.datasources.DataSource)如果方法上有对应的注解，拦截下来。
     *@within(com.example.datasources.DataSource)表示类上有注解，类中的方法拦截 */
    @Pointcut("@annotation(com.example.datasources.DataSource)||@within(com.example.datasources.DataSource)")
    public void pc(){

    }
    @Around("pc()")
    public Object around(ProceedingJoinPoint pjp){
        //获取方法上面的注解,如果没有看类上有没有（于是方法>类）。注意此处的DataSource是注解
        DataSource dataSource=getDataSource(pjp);
        if(dataSource!=null){
            //说明有注解（不管是方法还是类上)
            String value = dataSource.value();//获取数据源的value名称
            DynamicDataSourceContextHolder.setDataSourceType(value);
        }
        //pjp继续运行,最终记得清除
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }

    private DataSource getDataSource(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //查找方法上的注解.第二个参数是返回类型
        DataSource annotation = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if(annotation!=null){
            //说明方法上面有DataSource注解
            return annotation;
        }
        //获取方法所在的类
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(),DataSource.class);
    }
}
