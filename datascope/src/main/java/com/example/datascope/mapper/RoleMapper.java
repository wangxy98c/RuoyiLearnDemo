package com.example.datascope.mapper;

import com.example.datascope.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getAllRoles(Role role);
}
