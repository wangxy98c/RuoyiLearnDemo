package com.example.flowableprocess;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ActReTest {
    @Autowired
    RepositoryService repositoryService;
    private static final Logger logger= LoggerFactory.getLogger(ActReTest.class);
    @Test
    void test01(){
        //查询流程定义
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition pd : list) {
            logger.info("id:{};name:{};version:{};category:{}",pd.getId(),pd.getName(),pd.getVersion(),pd.getCategory());
        }
    }
    @Test
    void test02(){
        //查询所有流程的最新版本
        //List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        //根据xml文件中的id查询（对应RE_PROCDEF中的key）
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionKey("submit_an_expense_account").list();
        for (ProcessDefinition pd : list) {
            logger.info("id:{};name:{};version:{};category:{}",pd.getId(),pd.getName(),pd.getVersion(),pd.getCategory());
        }
    }
    @Test
    void test04(){
        List<Deployment> list = repositoryService.createDeploymentQuery().deploymentCategory("").list();
        //repositoryService.createNativeDeploymentQuery().sql("").parameter("","").list();
        //repositoryService.deleteDeployment(id);
        //repositoryService.activateProcessDefinitionById();
    }

}
