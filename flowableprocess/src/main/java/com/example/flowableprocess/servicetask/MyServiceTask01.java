package com.example.flowableprocess.servicetask;


import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

//自定义的监听器类,ServiceTask执行到这里会自动执行execute方法
public class MyServiceTask01 implements JavaDelegate {
    Expression username;//注意是 flowable..delegate里的.
    //且特别注意： 变量名必须对应ui/xml里的名字。不能随便取
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("===MyServiceTask01===getExpressionText"+username.getExpressionText());
        System.out.println("===MyServiceTask01===getValue"+username.getValue(delegateExecution));//多传入一个上下文
    }
}
