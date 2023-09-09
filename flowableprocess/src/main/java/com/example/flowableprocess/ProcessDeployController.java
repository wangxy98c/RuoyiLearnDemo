package com.example.flowableprocess;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
public class ProcessDeployController {
    @Autowired
    RepositoryService repositoryService;//可以操纵ACT_RE_XXX表
    @PostMapping("/deploy")
    public RespBean deploy(MultipartFile file) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name("Javaboy的工作流")//指_DEPLOYMENT中的name属性
                .category("这是我的流程分类")//指_DEPLOYMENT中的category属性,不是xml里的那个
                .key("自定义工作流的key")//指_DEPLOYMENT中的key属性
                //.tenantId("tenantId1")
                .addInputStream(file.getOriginalFilename(), file.getInputStream());//设置文件输入流 add可以有多种方式，此处用流
        Deployment deploy = deploymentBuilder.deploy();
        //如果使用addBytes时，名称不要随便取值，最好和文件名一致。否则容易出问题（或者说费力不讨好）
        return RespBean.ok("部署成功",deploy.getId());
    }
    @PostMapping("/deploy3")//定时任务测试(定时激活流程定义）
    public RespBean deploy3(MultipartFile file) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name("Javaboy的工作流")//指_DEPLOYMENT中的name属性
                .category("这是我的流程分类")//指_DEPLOYMENT中的category属性,不是xml里的那个
                .key("自定义工作流的key")//指_DEPLOYMENT中的key属性
                //.tenantId("tenantId1")
                //设置‘流程定义’激活的时间，到达这个时间之前它不可用。到达时间点后。可以启用
                .activateProcessDefinitionsOn(new Date(System.currentTimeMillis()+120*1000))//2分钟后激活
                .addInputStream(file.getOriginalFilename(), file.getInputStream());//设置文件输入流 add可以有多种方式，此处用流
        Deployment deploy = deploymentBuilder.deploy();
        //如果使用addBytes时，名称不要随便取值，最好和文件名一致。否则容易出问题（或者说费力不讨好）
        return RespBean.ok("部署成功",deploy.getId());
    }
    @PostMapping("/deploywithform")
    public RespBean deployWithForm(MultipartFile[] files) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .name("Javaboy的工作流")//指_DEPLOYMENT中的name属性
                .category("这是我的流程分类")//指_DEPLOYMENT中的category属性,不是xml里的那个
                .key("自定义工作流的key");//指_DEPLOYMENT中的key属性
                //.tenantId("tenantId1")
        for (MultipartFile file : files) {
             deploymentBuilder.addInputStream(file.getOriginalFilename(),file.getInputStream());
        }//使用接口的时候同时上传xml和html表单
                //.addInputStream(file.getOriginalFilename(), file.getInputStream());//设置文件输入流 add可以有多种方式，此处用流
        Deployment deploy = deploymentBuilder.deploy();
        return RespBean.ok("部署成功",deploy.getId());
    }
}
