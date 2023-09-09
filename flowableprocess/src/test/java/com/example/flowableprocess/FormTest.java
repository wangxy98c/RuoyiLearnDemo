package com.example.flowableprocess;

import liquibase.pro.packaged.A;
import org.flowable.engine.FormService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class FormTest {
    @Autowired
    FormService formService;
    @Autowired
    RepositoryService repositoryService;
    public static final Logger logger=LoggerFactory.getLogger(FormTest.class);
    @Test
    void test01(){
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("DynamicFormDemo01")
                .latestVersion().singleResult();
        StartFormData startFormData = formService.getStartFormData(pd.getId());//获取启动节点上的表单(没定义,故而没有）
        logger.info("流程部署的Id：{};开始节点表单key:{}",pd.getId(),startFormData.getFormProperties());
    }
    @Autowired
    RuntimeService runtimeService;
    @Test
    void test02(){
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExtFormDemo01").latestVersion().singleResult();
        String startFormKey = formService.getStartFormKey(pd.getId());//获取启动节上外置表单的key
        logger.info("startFormKey is :",startFormKey);
        //查询启动节点上，渲染之后的流程表单（主要针对外置表单，动态表单没用）
        String renderedStartForm = (String) formService.getRenderedStartForm(pd.getId());
        logger.info(renderedStartForm);
    }
    @Test
    void test08(){
        //启动外置表单流程，其实区别不大
        Map<String,String> vars=new HashMap<>();
        vars.put("days","10");
        vars.put("reason","请假");
        vars.put("startTime","2023-9-11");
        vars.put("endTime","2023-9-21");
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExtFormDemo01").latestVersion().singleResult();
        formService.submitStartFormData(pd.getId(),vars);
    }
    @Autowired
    TaskService taskService;
    @Test
    void test09(){//组长审批处获得的信息
        Task task = taskService.createTaskQuery().singleResult();
        String renderedTaskForm = (String) formService.getRenderedTaskForm(task.getId());
        logger.info("renderedTaskForm:{}",renderedTaskForm);
    }
    @Test
    void test10(){//组长审批处提交表单
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, String> vars=new HashMap<>();
        vars.put("days","100");
        formService.submitTaskFormData(task.getId(),vars);
    }
}
