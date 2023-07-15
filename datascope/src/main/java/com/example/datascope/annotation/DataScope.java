package com.example.datascope.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    String deptAlias() default "";//别名，比如Sql语句中的 sys_dept d .....where d.xxx
    String userAlias() default "";//同上

}
