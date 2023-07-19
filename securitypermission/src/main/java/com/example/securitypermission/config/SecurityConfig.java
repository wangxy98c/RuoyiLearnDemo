package com.example.securitypermission.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    UserDetailsService us(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("testsuser").password("{noop}123")
                //给用户设置角色
                .roles("admin")
                //设置两个权限
                //.authorities("sys:user:add","sys:user:delete")//注意默认没有sys:user:**这种写法，不会当作通配符。那么我们就需要自己在权限评估器中自己写
                .authorities("sys:user:*")
                .build());//{noop}表示不使用加密

        return manager;
    }
    /*为了使用自定义的CustomSecurityExpressionRoot
    * 完成后
    * 此时，@PreAuthorize中的方法就被替换为自定义的方法了。且可以用自己新名称的方法*/
    @Bean
    DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(){
        return new CustomMethodSecurityExpressionHandler();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().anyRequest().authenticated().and().formLogin().permitAll();
        return http.build();
    }
}
