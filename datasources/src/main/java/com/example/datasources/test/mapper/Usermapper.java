package com.example.datasources.test.mapper;

import com.example.datasources.test.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface Usermapper {
    @Select("select * from user")
    List<User> getAllUsers();
}
