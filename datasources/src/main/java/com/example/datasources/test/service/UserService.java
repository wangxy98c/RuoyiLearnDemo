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
    @DataSource("slave")
    public List<User> getAllUsers(){
        return usermapper.getAllUsers();
    }
}
