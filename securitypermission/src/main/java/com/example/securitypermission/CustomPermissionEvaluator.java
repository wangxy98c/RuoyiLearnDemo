package com.example.securitypermission;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.Serializable;
import java.util.Collection;

/*自定义的权限评估器
* 只需要将自定义的权限评估器注册到Spring容器中就会生效*/
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    AntPathMatcher antPathMatcher=new AntPathMatcher();//为了自己实现通配符,一定要new啊。。不new就会空指针
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        //获取当前用户具备的Authorities
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {//那就判断有没有所需要的吧
            /*if(authority.getAuthority().equals(permission)){
                return true;
            }*/
            if(antPathMatcher.match(authority.getAuthority(), ((String) permission) ) ){
                return true;//自己写支持通配符的权限
            }
        }
        System.out.println("权限对不上");
        return  false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
