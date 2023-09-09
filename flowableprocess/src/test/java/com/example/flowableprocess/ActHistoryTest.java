package com.example.flowableprocess;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.entitylink.api.history.HistoricEntityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ActHistoryTest {
    @Autowired
    RuntimeService runtimeService;
    @Test
    void test01(){//启动
        Authentication.setAuthenticatedUserId("lisi");//设置发起人
        runtimeService.startProcessInstanceByKey("HistoryDemo01");
    }
    @Autowired
    TaskService taskService;
    @Test
    void test02(){
        Task task = taskService.createTaskQuery().taskAssignee("lisi").singleResult();
        Map<String, Object> vars=new HashMap<>();
        vars.put("days",10);
        vars.put("reason","出去玩");
        taskService.complete(task.getId(),vars);
    }
    @Test
    void test03(){//test1 2 3 都是准备并完成流程，用于查询历史记录
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        Map<String, Object> vars=new HashMap<>();
        vars.put("state","审批通过");
        taskService.complete(task.getId(),vars);
    }
    @Autowired
    HistoryService historyService;
    @Test
    void test04(){//查询历史流程
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance hpi : list) {
            System.out.println("name/id:"+hpi.getName()+hpi.getId());
        }
    }
    @Test
    void test05(){//查询历史任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee("zhangsan").finished().list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("name/id:"+historicTaskInstance.getName()+" "+historicTaskInstance.getId());
        }
    }
    @Test
    void test06(){//查询历史活动
        List<HistoricProcessInstance> processInstances = historyService.createHistoricProcessInstanceQuery().finished().list();
        for (HistoricProcessInstance pi : processInstances) {
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(pi.getId()).list();
            for (HistoricActivityInstance hai : list) {
                //System.out.println(hai.get);
            }
        }
    }
    @Test
    void test07(){
        List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().finished().list();

    }
    @Test
    void test08(){
        historyService.createNativeHistoricProcessInstanceQuery().sql("");
        historyService.createNativeHistoricTaskInstanceQuery().sql("");
    }

}
