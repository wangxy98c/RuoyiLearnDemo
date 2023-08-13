# 部署
## /resources/processes文件夹下的所有流程文件默认会被自动部署
### 跟流程部署相关的表以 `ACT_RE_` 为前缀
`ACT_RE_DEPLOYMENT` `ACT_RE_PROCDEF` 分别保存了流程定义的相关信息  
`ACT_GE_BYTEARRAY` 保存了流程的 **xml文件**以及**根据xml文件生成的流程图**
### 流程部署后可以直接在xml中修改，重启后会自动更新
    数据库新添加记录，且version会自增1.
1. xml文件中的`targetNamespace`其实就是流程的分类定义（数据库PROCDEF中的category字段），如果想要修改流程定义的分类，直接修改它即可
## 自动部署的自定义配置
1. 项目启动时是否自动检查流程图文件。不检查即不会自动部署流程了
   flowable.check-process-definitions=true
2. 设置流程文件的位置
   flowable.process-definition-location-prefix=classpath*:/processes/
3. 指定流程文件的后缀 
   flowable.process-definition-location-suffixes=**.bpmn20.xml,**.bpmn
## 手动部署
项目启动成功后，再去部署流程。常用接口来部署.  
参见PROcessDeployController
# 查询
`RepositoryService repositoryService`
## 查询流程定义相关信息（_RE_PROCDEF）
`repositoryService.createProcessDefinitionQuery().list();` 查询流程
`repositoryService.createProcessDefinitionQuery().latestVersion().list();` 查询所有流程的最新版本
`repositoryService.createProcessDefinitionQuery().processDefinitionKey("submit_an_expense_account").list();`根据xml文件中的id查询（对应RE_PROCDEF中的KEY）
### 自定义查询流程定义信息(_RE_PROCDEF)
`repositoryService.createNativeProcessDefinitionQuery().sql("").parameter().list;` 自定义查询语句。
### 查询流程部署信息(_RE_deployment)
`repositoryService.createDeploymentQuery().list()`查询所有
`repositoryService.createDeploymentQuery().deploymentCategory("").list();` 根据分类查询
### 自定义查询部署信息（_RE_deployment）
`repositoryService.createNativeDeploymentQuery().sql("").parameter("","").list();`
`repositoryService.deleteDeployment(id);` 删除  
**以上都可以通过嵌套for循环拿到某些信息在此查询来实现更复杂的功能**  
# 流程运行(参见ACTRUTest)
## 流程启动
**流程运行中设计到的表都是以`ACT_RU_`前缀开头,且流程执行完毕后这些数据都会被清除**
`RuntimeService runtimeService;`
`runtimeService.startProcessInstanceByKey(processDefinitionKey)` key是xml中的id
注意：视频中需要手动改变量为${}，但实际不需要，$即可
流程启动成功后，`ACT_RU_EXECUTION` 表中保存了所有流程执行的信息(包括启动节点和其他任务节点)  
同时，如果这个节点是UserTask，则这个节点的信息还会保存在`ACT_RU_TASK`中（该表用来保存userTask）  
还有`ACT_TU_ACTINST`表中还会保存流程的执行情况。
### 设置发起人的两种方式
1. `Authentication.setAuthenticatedUserId("wangwu");`
2. `identityService.setAuthenticatedUserId("wangwu");`
### 任务处理
`taskService.createTaskQuery().taskAssignee("wangwu").list();`
`taskService.complete(task.getId());`
### 判断是否结束
借助数据库的表数据来判断
### 删除流程实例(正在运行的不想要了)
`runtimeService.deleteProcessInstance("processInstanceId","the reason of delete");`
`runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();` ==null则运行完成
### 查看运行的活动节点(查看走到哪一步了)
```java
List<Execution> list = runtimeService.createExecutionQuery().list();
for (Execution execution : list) {
   List<String> activeActivityIds = runtimeService.getActiveActivityIds(execution.getId());
}//然后它就是执行到了哪一步（可能多条路线？）
```

# 挂起、恢复
## 流程定义
`repositoryService.isProcessDefinitionSuspended(pd.getId())` 判断是否挂起
`repositoryService.suspendProcessDefinitionById();` 挂起
`repositoryService.activateProcessDefinitionById();` 激活
**其实就是修改_RE_PROCDEF中的SUSPENSTION_STATE字段 1表示激活的，2表示挂起的**
## 流程实例
挂起和激活其实也是上面的函数，不过参数不同。（比如第二个参数是 “是否挂起对应的实例”， 第三个参数 是何时被挂起）
**涉及到 `_RU_EXECUTION` `_RU_TASK` `_RE_PROCDEF` 三张表**

