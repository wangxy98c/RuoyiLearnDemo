package com.example.flowableprocess;

import liquibase.pro.packaged.A;
import org.flowable.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTaskTest {
    @Autowired
    RuntimeService runtimeService;
    @Test
    void test01(){
        runtimeService.startProcessInstanceByKey("ServiceTaskDemo01");
    }
}
