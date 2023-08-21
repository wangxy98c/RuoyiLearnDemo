package com.example.flowableprocess;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ACTRUTest {
    @Autowired
    RuntimeService runtimeService;
    public static final Logger logger= LoggerFactory.getLogger(ACTRUTest.class);
    @Test
    void test01(){
        //设置流程发起人
        //Authentication.setAuthenticatedUserId("wangwu");
        identityService.setAuthenticatedUserId("wangwu");//或者这个
        //它实际上是xml文件中的流程ID:<process id="leave" name="请假流程图" isExecutable="true">
        String processDefinitionKey="leave";
        ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        //runtimeService.startProcessInstanceById(processDefinitionId);//也可以用流程定义的ID，可用查询获得（不是xml文件中的id）
        logger.info("definitionId:{};id:{};name:{}",pi.getProcessDefinitionId(),pi.getId(),pi.getName());
    }
    @Autowired
    IdentityService identityService;
    @Test
    void test02(){

    }
    @Autowired
    TaskService taskService;
    @Test
    void test03(){
        //查询“wangwu“需要执行的任务并处理
        List<Task> list = taskService.createTaskQuery().taskAssignee("wangwu").list();
        System.out.println(list.size());
        for (Task task : list) {
            logger.info("id:{},name:{};assignee:{}",task.getId(),task.getName(),task.getAssignee());
            taskService.complete(task.getId());//完成任务
        }
    }//同理查询zhangsan、lisi
    @Test
    void test04(){
        //如果_RU_EXECUTION 表中没有这个流程的记录，则说明结束
        String processId="";
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if(pi==null){

        }
    }
    @Test
    void test05(){
        List<Execution> list = runtimeService.createExecutionQuery().list();
        for (Execution execution : list) {
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(execution.getId());
        }
    }
    @Test
    void test06(){
        runtimeService.deleteProcessInstance("ProcessInstanceId","the reason of delete");

    }
    @Test
    void tst10(){
        //runtimeService.getDataObject()
    }
}
