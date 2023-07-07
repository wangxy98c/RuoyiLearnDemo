package com.example.datasources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*定义一个注解*/
/*这个注解将来可以注解Service或类上，通过value属性指定类或者方法应该使用哪个数据源*/
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {
    String value() default "master";//有@DataSource但没有vlue就用默认的"master"
}
