package com.example.flowableprocess;

import liquibase.pro.packaged.R;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
public class GateWayTest {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Test
    void test01(){
        Map<String, Object> map=new HashMap<>();
        map.put("days",3);
        runtimeService.startProcessInstanceByKey("ExclusiveGatewayDemo01",map);
    }
    @Test
    void test02(){
        List<Task> list = taskService.createTaskQuery().list();
        for (Task task : list) {
            taskService.complete(task.getId());
        }
    }
}
