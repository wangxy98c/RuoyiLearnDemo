package com.example.flowableprocess;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReceiveTaskTest {
    @Autowired
    RuntimeService runtimeService;
    public static final Logger logger=LoggerFactory.getLogger(ReceiveTaskTest.class);
    @Test
    void test01(){
        ProcessInstance pi = runtimeService.startProcessInstanceByKeyAndTenantId("ReceiveTaskDemo","tenantId1");
        //这个部署的时候有租户，后面再测试就没设置了。
        logger.info("id:{};name:{}",pi.getId(),pi.getName());
    }
    @Test
    void test02(){//trigger
        //List<Execution> list = runtimeService.createExecutionQuery().activityId("sid-5D2CE959-24B6-4BFF-8170-273925993060").list();
        //上面receiveTask活动"统计今日销售额"的id忘记取名了，从xml里复制
        List<Execution> list = runtimeService.createExecutionQuery().activityId("sid-485DBC16-361D-4488-84C8-3599BCB4B1DA").list();
        //自动发送统计报告的活动id
        for (Execution execution : list) {
            String executionId;
            runtimeService.trigger(execution.getId());//继续向下执行，参数是executionId，需要查询
        }
    }
}
