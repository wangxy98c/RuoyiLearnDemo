package com.example.datasources.alterDS;

import com.example.datasources.test.DataSourceType;
import com.example.datasources.test.model.User;
import com.example.datasources.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class DataSourceController {
    @Autowired
    UserService userService;
    private static final Logger logger= LoggerFactory.getLogger(DataSourceController.class);
    /* 修改当前会话的数据源,且新的会话不会受到影响
    * （也可以和用户绑定，来实现该用户的所有会话都修改数据源）*/
    @PostMapping("/dstype")
    public void setDSType(String dsType, HttpSession session){
        System.out.println("dstype runing");
        session.setAttribute(DataSourceType.DS_SESSION_KEY,dsType);
        logger.info("数据源切换为{}",dsType);
    }
    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
