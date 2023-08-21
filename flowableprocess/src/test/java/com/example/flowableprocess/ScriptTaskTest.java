package com.example.flowableprocess;

import org.flowable.engine.RuntimeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ScriptTaskTest {
    @Autowired
    RuntimeService runtimeService;
    @Test
    void test01(){//js\groovy 都可用此测试（尽管groovy没有a、b）
        //需要传入变量 a、b用于计算sum
        Map<String, Object> map = new HashMap<>();
        map.put("a",99);map.put("b",100);
        runtimeService.startProcessInstanceByKey("ScriptTaskDemo01",map);
    }
}
