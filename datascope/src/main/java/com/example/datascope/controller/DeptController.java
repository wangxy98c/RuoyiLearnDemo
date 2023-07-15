package com.example.datascope.controller;

import com.example.datascope.entity.Dept;
import com.example.datascope.service.IDeptService;
import jdk.xml.internal.XMLSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    IDeptService deptService;
    @GetMapping("/")
    public List<Dept> getAllDepts(Dept dept){//dept.params可以从前端传入，需要防止Sql注入
        return deptService.getAllDepts(dept);
    }
}
