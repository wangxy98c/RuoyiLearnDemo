package com.example.datascope.controller;

import com.example.datascope.entity.Role;
import com.example.datascope.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    IRoleService roleService;
    @GetMapping("/")
    public List<Role> getAllRoles(Role role){
        System.out.println("====>>>Controller");
        return roleService.getAllRoles(role);
    }
}
