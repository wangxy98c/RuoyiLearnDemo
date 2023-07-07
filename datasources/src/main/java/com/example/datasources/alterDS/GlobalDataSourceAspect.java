package com.example.datasources.alterDS;

import com.example.datasources.DynamicDataSourceContextHolder;
import com.example.datasources.test.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/*此时，有两个切面了。那么在方法执行前哪个会先被执行呢？看项目如何设计的
* 本demo中，必然是网页切换数据源的优先级高。设置@Order。数字越小执行优先级越高(数字越小越先执行，后面执行会覆盖Holder)*/
@Aspect
@Component
@Order(13)//数字更大，更靠后执行。以此为准。如果它更小，会有如下情况（当方法上添加注解，网页修改源则不生效)
public class GlobalDataSourceAspect {
    @Autowired
    HttpSession httpSession;
    @Pointcut("execution(* com.example.datasources.test.service.*.*(..))")//拦截service里的所有方法。execution：执行时触发
    public void pc(){//它与上面注解配合，形成可重用的切点表达式。如果不使用它，则每次都需要Around("execution......")比较麻烦

    }
    @Around("pc()")//等同于@Around("execution(* com.example.datasources.alterDS.*.*(..))")
    public Object around(ProceedingJoinPoint pjp){
        //从session里拿出来源名，放入Holder里
        DynamicDataSourceContextHolder.setDataSourceType((String) httpSession.getAttribute(DataSourceType.DS_SESSION_KEY));
        try {
            return pjp.proceed();//被拦截的方法继续向后走
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceType();
        }
    }
}
