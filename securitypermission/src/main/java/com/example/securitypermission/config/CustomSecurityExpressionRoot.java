package com.example.securitypermission.config;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
/*默认使用的是SecurityExpressionRoot
要使用这个类来代替，需要额外配置
* */
public class CustomSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private AntPathMatcher antPathMatcher=new AntPathMatcher();//不要忘记new
    public CustomSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    //写自己的判断
    public boolean hasPermission(String permission){
        if(permission==null||permission.length()==0){
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//来自父类
        for (GrantedAuthority authority : authorities) {
            String s = authority.getAuthority();
            if(antPathMatcher.match(s,permission)){
                return true;
            }
        }
        return false;
    }
    public boolean hasAnyPermission(String... permissions){
        if(permissions==null || permissions.length==0){
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            for (String permission : permissions) {
                if(antPathMatcher.match(authority.getAuthority(),permission)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean hasAllPermission(String... permissions){
        if(permissions==null ||permissions.length==0 ){
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (String permission : permissions) {
            boolean tag=false;
            for (GrantedAuthority authority : authorities) {
                //System.out.println("1=====>\n"+permission+"==?=="+authority.getAuthority());
                if(antPathMatcher.match(authority.getAuthority(),permission)){//注意，顺序不能写反！！。A能不能匹配上B（*匹配b）
                    //System.out.println("2=====>匹配成功");
                    tag=true;
                }
            }
            if(tag==false){
                //System.out.println("3=====>此刻tag："+tag);
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject=filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject=returnObject;
    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
