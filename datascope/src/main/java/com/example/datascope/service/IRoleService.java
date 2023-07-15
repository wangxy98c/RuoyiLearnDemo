package com.example.datascope.service;

import com.example.datascope.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务类
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
public interface IRoleService extends IService<Role> {

    List<Role> getAllRoles(Role role);
}
