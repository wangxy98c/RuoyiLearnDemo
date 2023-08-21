package com.example.flowableprocess;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class UserTaskTest {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;//注意不是task.包
    public static final Logger logger= LoggerFactory.getLogger(UserTaskTest.class);
    @Test
    void test01(){//启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo");
        logger.info("id:{};name:{}",pi.getId(),pi.getName());
    }
    @Test
    void test02(){//委派
        //查询javaboy需要处理的
        List<Task> list = taskService.createTaskQuery().taskAssignee("javaboy").list();
        for (Task task : list) {
            logger.info("name:{};assignee:{}",task.getName(),task.getAssignee());
            //两种处理思路：1. 委派（其实是修改assignee） 2.自己处理
            taskService.setAssignee(task.getId(),"zhangsan");//委派
        }
    }
    @Test
    void test03(){//自己处理
        //修改成了zhangsan，zhangsan自己处理
        List<Task> list = taskService.createTaskQuery().taskAssignee("zhaoliu").list();
        for (Task task : list) {
            logger.info("name:{};assignee:{}",task.getName(),task.getAssignee());
            //zhangsan自己处理
            taskService.complete(task.getId());
        }
    }
    @Test
    void test05(){//启动时指定处理人，需要在画图设置分配用户时设置成 ${manager} 或在xml直接修改
        Map<String, Object> vars = new HashMap<>();
        vars.put("manager","lisi");
        runtimeService.startProcessInstanceByKey("UserTaskDemo",vars);
    }
    @Test
    void test06(){//启动流程并设置发起人
        Authentication.setAuthenticatedUserId("zhaoliu");//流程启动时会将 zhaoliu 设置为发起人
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("UserTaskDemo");
        logger.info("id:{};name:{}",pi.getId(),pi.getName());
    }
    @Test
    void test08(){//候选人的查询方法。
        List<Task> zhangsan = taskService.createTaskQuery().taskCandidateUser("zhangsan").list();
    }
    @Test
    void test09(){//查询流程的参与者
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().singleResult();
        List<IdentityLink> links = runtimeService.getIdentityLinksForProcessInstance(pi.getId());
        for (IdentityLink link : links) {
            //各个参与人
        }
    }
    @Test
    void test10(){//候选人认领任务,其实也会查询所有zhangsan所在的组，然后在某个组也算候选
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("zhangsan").list();
        for (Task task : tasks) {
            taskService.claim(task.getId(),"zhangsan");
        }
    }
    @Autowired
    IdentityService identityService;
    @Test
    void test16(){//按角色（其实是组）分配，先准备数据
        UserEntityImpl u1 = new UserEntityImpl();
        u1.setRevision(0);u1.setEmail("zhangsan@qq.com");u1.setPassword("123");u1.setId("zhangsan");u1.setDisplayName("张三");
        identityService.saveUser(u1);

        UserEntityImpl u2 = new UserEntityImpl();
        u2.setRevision(0);u2.setEmail("lisi@qq.com");u2.setPassword("123");u2.setId("lisi");u2.setDisplayName("李四");
        identityService.saveUser(u2);

        GroupEntityImpl g1 = new GroupEntityImpl();
        g1.setRevision(0);g1.setId("manager");g1.setName("经理");
        identityService.saveGroup(g1);

        identityService.createMembership("zhangsan","manager");
        identityService.createMembership("lisi","manager");
    }
    @Test
    void test17(){

    }
}
