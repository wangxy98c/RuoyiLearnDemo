package com.example.securitypermission.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserController {
    @RequestMapping("/add")//##note 注意都是Request，而不是get、post、put、等
    //@PreAuthorize("hasPermission('/add','sys:user:add')")
//    @PreAuthorize("hasAuthority('sys:user:add')")
    @PreAuthorize("hasPermission('sys:user:add')")//不会提示自己写的方法，但不会报错的
    public String add(){
        return "add";
    }
    @RequestMapping("/delete")
    //@PreAuthorize("hasPermission('/delete','sys:user:delete')")
//    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PreAuthorize("hasAnyPermission('sys:user:add','sys:user:delete')")
    public String delete(){
        return "delete";
    }
     /*##note
     @PreAuthorize("hasPermission('/add','sys:user:add')")按理说add和delete可以访问的（内存用户有这两个权限）。但为什么实际不能呢？
     结合spel，没有写成@xxx.hasPermission（指定对象）；说明是spel的rootObject（SecurityExpressionRoot）中hasPermission的方法
     这个方法又调用了PermissionEvaluator.hasPermission方法（接口中的方法，但它只有一个DenyAllPermission的实Evaluator实现类，自然就只能用这个）
     这个实现类压根不判断，直接返回false。从而导致了该问题，我们需要自己写一个权限评估器
    */
    @RequestMapping("/update")
    //@PreAuthorize("hasPermission('/update','sys:user:update')")
//    @PreAuthorize("hasAuthority('sys:user:update')")
    @PreAuthorize("hasAllPermission('sys:user:add','sys:user:delete')")
    public String update(){
        return "update";
    }
    @RequestMapping("/select")
    //@PreAuthorize("hasPermission('/select','sys:user:select')")
//    @PreAuthorize("hasAuthority('sys:user:select')")
    @PreAuthorize("hasAllPermission('sys:user:select','sys:user:add','sys:user:delete')")
    public String select(){
        return "select";
    }
}
