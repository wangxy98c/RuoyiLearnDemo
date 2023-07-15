package com.example.datascope.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.datascope.entity.User;
import com.example.datascope.mapper.UserMapper;
import com.example.datascope.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService , UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //baomidou提供的查询方法
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.lambda().eq(User::getUserName,username);
        User user = getOne(qw);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在");
        }
        //security5 默认需要加密。此处设置为不加密（在password字段前添加{noop}即可）
        user.setPassword("{noop}"+user.getPassword());
        user.setRoles(userMapper.getRolesByUid(user.getUserId()));
        System.out.println(user);
        return user;
    }
}
