**笔记有点乱**

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
# 流程运行

**流程运行中设计到的表都是以`ACT_RU_`前缀开头,且流程执行完毕后这些数据都会被清除**
`RuntimeService runtimeService;`
`runtimeService.startProcessInstanceByKey(processDefinitionKey)` key是xml中的id
注意：直接下载下来的文件有点小问题，变量需要变为 #{INITATOR} 
流程启动成功后，`ACT_RU_EXECUTION` 表中保存了所有流程执行的信息(包括启动节点和其他任务节点)  
同时，如果这个节点是UserTask，则这个节点的信息还会保存在`ACT_RU_TASK`中（该表用来保存userTask）  
还有`ACT_TU_ACTINST`表中还会保存流程的执行情况。

### 设置发起人的两种方式

1. `Authentication.setAuthenticatedUserId("wangwu");`
2. `identityService.setAuthenticatedUserId("wangwu");`
### 候选人/组

#### 定义候选人。

ui中直接设置或xml里的`candidateUsers=“zhangsan,lisi,wangwu”`。但查询不能再使用之前的方案（因为assignee为空）

#### 查询候选人的可认领任务

`taskService.createTaskQuery().taskCandidateUser("zhangsan").list();`候选人查询，**在某个组也算候选**

#### 通过组查询候选人任务

`taskService.createTaskQuery().taskCandidateGroup("manager").list()` 直接用组查询候选人（注意上面是User，这里是Group）

#### 通过变量指定候选组是哪个组（不在xml里写死）

与其他的类似，在ui里的候选组使用`${g}`。然后`map.put("g","manager")`。再`runtimeService.startProcessInstanceByKey("UserTaskDemo",map)`在启动时设置某活动的候选组是哪个组

#### 添加与删除候选人

### 根据流程ID查询流程参与者(候选人)

```java
ProcessInstance pi = runtimeService.createProcessInstanceQuery().singleResult();
List<IdentityLink> links =runtimeService.getIdentityLinksForProcessInstance(pi.getId());
for (IdentityLink link : links) {
    //各个参与人
}
```

### 任务处理

`taskService.createTaskQuery().taskAssignee("wangwu").list();`

`taskService.complete(task.getId());`

---

当只有候选人时，不能用complete。需要先**认领**（其实就是设置assignee，但不是setAssignee方法而是cliam方法）再处理。**需要注意的是如果Assignee有人了，则无法认领**

```java
List<Task> tasks = taskService.createTaskQuery().taskCandidateUser("zhangsan").list();
for (Task task : tasks) {
    taskService.claim(task.getId(),"zhangsan");
}
```

### 任务认领回退

已经认领的任务回退，让其他人认领。其实就是置为null

`taskService.setAssignee(task.getId(),null)`

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
# DataObject
数据对象（不选择任何节点）本质上是给流程设置一些全局属性。
流程启动后，这些属性（以及其他变量，如INTIATOR)会在`_RU_VARIABLE`中记录
## 使用
`runtimeService.getDataObject(execution.getId())`.得到一个map，用.keySet拿到键值后遍历各个变量。可以用getValue等取得实际变量值  
# 租户 Tenant
假如有4个子系统，它们都需要相同的流程。利用租户tenant进行区分
部署时 `repositoryService.tenantId("javaboy"")` 参见Controller中的部署代码。部署成功后可以在`_RE_PROCDEF`中看到`TENANT_ID`
一个流程在定义时如果指定了租户ID，那么在 **启动**时也必须指定租户ID。执行任务不需要指定  
`runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey,"javaboy");` 需要添加一个TenantID参数
虽然执行不需要指定租户，但是可以通过租户查询Task`taskService.createTaskQuery().taskTenantId("javaboy").list()`
# 流程任务
## ReceiveTask：人工trigger（ui里点左下角小扳手）

接收任务，一般不需要做什么（可能只是系统无法自动判断，需要借助人来判断）。但需要用户手动trigger一下（点一下）。
所以receiverTask就是让任务停一下，然后人工判断是否继续走。且并不指定具体的人。

## UserTask：最常见的

### 单个用户

1. 直接指定具体用户：固定中设置
2. 通过变量来设置：可以在**流程启动时通过变量可以指定处理人**
3. 通过监听器设置：可以在**特定时刻设置一个处理人**可选时刻如下![image-20230817163053699](./RECORD.assets/image-20230817163053699.png)

   ![image-20230817162005464](./RECORD.assets/image-20230817162005464.png)
4. 设置为流程的发起人：

   ![image-20230817203356388](./RECORD.assets/image-20230817203356388.png)

   设置的是一个变量，变量的名称就是`INITATOR`

   **其实就是流程启动中设置发起人的两种方式，以上是其中一种**

### 多个用户/候选用户

在候选里用变量即可，如：`${userIds}`。然后使用时`map.put("userIds","zhansan,lisi,wangwu")`作为`startProcessInstanceByKey`的第二个参数即可

如果使用监听器：则`delegateTask.addCandidateUser("zhangsan");`注意一次添加一个

## ServiceTask

系统自动完成的任务，流程走到这一步会自动执行。

### 监听类

1. 首先定义一个实现了JavaDelegate的监听器类MyServiceTask01

2. 在绘制流程图时为ServiceTask配置监听器类引用，如：com.example.flowableprocess.servicetask.MyServiceTask01

   ![image-20230818145528056](./RECORD.assets/image-20230818145528056.png)

   流程已启动到达ServiceTask活动即可自动触发execute方法。且ServiceTask执行中不会保存到_RU_TASK中

#### 监听类里的变量/属性

![image-20230818151257907](./RECORD.assets/image-20230818151257907.png)

```java
public class MyServiceTask01 implements JavaDelegate {
    Expression username;//注意是 flowable..delegate里的.
    //且特别注意： 变量名必须对应ui/xml里的名字。不能随便取
    @Override
    public void execute(DelegateExecution delegateExecution) {
  			System.out.println("===MyServiceTask01===getExpressionText"+username.getExpressionText());
    		System.out.println("===MyServiceTask01===getValue"+username.getValue(delegateExecution));//多传入一个上下文
    }
}
```

**特别注意的是：代码里的变量名必须和ui/xml文件中的'名称' 对应**

### 委托表达式

类似监听器类，但是可以将类注册到Spring容器中，然后给流程图配置的时候直接配置Bean名称即可（不再需要是完整引用了）

**其实就是类前加个注解 @Component**。然后在ui里的“委托表达式”（不再是‘类’了，且注意清空‘类’）![image-20230818153152494](./RECORD.assets/image-20230818153152494.png)



### 表达式

以上两种（完整引用和委托表达式）都**离不开JavaDelegate接口**。如果只是一个**普通的Bean其实也可以配置为ServiceTask的执行类**。

![image-20230818155811607](./RECORD.assets/image-20230818155811607.png)

## ScriptTask

脚本任务ScriptTask和ServiceTask类似也是自动执行的，不同之处在于脚本任务的逻辑通过一些非Java的脚本语言来实现

### JavaScript

![image-20230819173634619](./RECORD.assets/image-20230819173634619.png)

其中：execution.setVariable保存一个名为sum的流程变量

> 注意：不能使用let关键字，以及java17不再自带javascript的问题。会遇到`org.flowable.common.engine.api.FlowableException: Can't find scripting engine for 'JavaScript'`问题。原因如下For everyone that does not understand how to solve this and why the problem is there. **Starting from Java 15 the Javascript Nashorn engine has been removed from the JDK JEP 372: Remove the Nashorn JavaScript Engine 10.If you want to keep using Javascript you will need to manually add Maven Central Repository Search**
>
> 添加依赖可获得javascript支持
>
> ```xml
> <dependency>
>     <groupId>org.openjdk.nashorn</groupId>
>     <artifactId>nashorn-core</artifactId>
>     <version>15.4</version>
> </dependency>
> ```

由于流程直接执行完毕，可以到_HI_VARINST查看历史记录中的变量值。

### Groovy

Groovy是基于JVM的编程语言，写java也可以运行。添加依赖

```xml
<dependency>
  <groupId>org.codehaus.groovy</groupId>
  <artifactId>groovy-all</artifactId>
  <version>3.0.13</version>
</dependency>
```

### Juel

全称JavaUnifiedExpressionLanguage。${xx}其实就是juel。

`${myServiceTask03.hello()}`即脚本内容。可用Bean的方法，类似《表达式》。

# 网关

## 排他网关

最常见的一种，也叫互斥网关。可以有多个入口，但只有一个出口。![image-20230820221632155](./RECORD.assets/image-20230820221632155.png)

![image-20230820221555892](./RECORD.assets/image-20230820221555892.png)



## 并行网关

**并行网关是成对出现的**（开始和结束、分散和汇集）。其他的主要是画图，没什么特别的。

## 包容网关

可以自动根据具体条件，**自动转为上面两种**（排他网关、并行网关）

# 全局流程变量

## 启动时设置