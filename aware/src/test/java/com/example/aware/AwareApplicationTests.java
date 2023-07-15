package com.example.aware;

import com.example.aware.service.BeanUtils;
import com.example.aware.service.BeanUtils2;
import com.example.aware.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AwareApplicationTests {

    @Test
    void contextLoads() {
        //方法1
        //UserService userService = BeanUtils.getBean("userService");
        //userService.sayHello();

        //方法2
        UserService bean = BeanUtils2.getBean("userService");
        bean.sayHello();
    }


}
