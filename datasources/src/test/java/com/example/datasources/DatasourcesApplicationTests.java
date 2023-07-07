package com.example.datasources;

import com.example.datasources.test.model.User;
import com.example.datasources.test.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DatasourcesApplicationTests {

    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        List<User> list=userService.getAllUsers();
        for (User user : list) {
            System.out.println(user);
        }
    }



}
