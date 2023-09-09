package com.example.flowableprocess;

import org.apache.commons.io.FileUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.cmmn.image.impl.DefaultCaseDiagramGenerator;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ProcessImageTest {
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    HistoryService historyService;
    @Test
    void test01() throws IOException {//把一个流程定义绘制成一张图并输出成文件
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("").latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        InputStream stream = generator.generatePngDiagram(bpmnModel, 1.0, true);//流程图对象、缩放因子、是否绘制连接线上的文本
        FileUtils.copyInputStreamToFile(stream,new File("/path/pngname.png"));
        stream.close();
    }
    @Test
    void test02(){
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("").latestVersion().singleResult();
        //注意此处最好判断是不是空，空就不需要生成直接返回即可
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        //以上内容同绘制，进度的区别在下面.
        //查询当前流程下所有的活动信息（已经经过的活动会记录在数据库中，被用于判断并绘制）
        List<String> highLightedActivities=new ArrayList<>();
        List<String> highLightedFlows=new ArrayList<>();
        List<ActivityInstance> list = runtimeService.createActivityInstanceQuery().processInstanceId(processDefinition.getId()).list();
        for (ActivityInstance activityInstance : list) {
            //拿到每个，判断各种类型并处理
            if(activityInstance.getActivityType().equals("sequenceFlow")){
                highLightedFlows.add(activityInstance.getActivityId());
            }else{
                highLightedActivities.add(activityInstance.getActivityId());
            }
        }
        double scaleFactor=1.0;
        boolean drawSequeneFlowNameWithNoLabelDI=true;//以上是各种参数配置
        generator.generateDiagram(bpmnModel,"PNG",highLightedActivities,highLightedFlows,scaleFactor,drawSequeneFlowNameWithNoLabelDI);
    }
    @Test
    void test03(){
        historyService.
    }
}
