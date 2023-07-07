package com.example.datasources.test.service;

import com.example.datasources.DataSource;
import com.example.datasources.test.mapper.Usermapper;
import com.example.datasources.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    Usermapper usermapper;
    @DataSource("slave") //这里的注解是否总是生效与两个切面的优先级有关。
    public List<User> getAllUsers(){
        return usermapper.getAllUsers();
    }
}
