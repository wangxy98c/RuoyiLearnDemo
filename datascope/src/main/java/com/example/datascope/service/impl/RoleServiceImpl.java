package com.example.datascope.service.impl;

import com.example.datascope.annotation.DataScope;
import com.example.datascope.entity.Role;
import com.example.datascope.mapper.RoleMapper;
import com.example.datascope.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色信息表 服务实现类
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    RoleMapper roleMapper;
    @Override
    @DataScope(deptAlias = "d",userAlias = "u")
    public List<Role> getAllRoles(Role role) {
        return roleMapper.getAllRoles(role);
    }
}
