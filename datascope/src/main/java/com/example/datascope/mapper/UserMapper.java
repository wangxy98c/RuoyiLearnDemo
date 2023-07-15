package com.example.datascope.mapper;

import com.example.datascope.entity.Role;
import com.example.datascope.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户信息表 Mapper 接口
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
public interface UserMapper extends BaseMapper<User> {

    List<Role> getRolesByUid(Long userId);
}
