package com.example.datascope.service;

import com.example.datascope.entity.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
public interface IDeptService extends IService<Dept> {

    List<Dept> getAllDepts(Dept dept);
}
