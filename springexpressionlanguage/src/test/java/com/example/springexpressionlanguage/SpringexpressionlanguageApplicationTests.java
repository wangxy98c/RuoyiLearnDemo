package com.example.springexpressionlanguage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@SpringBootTest
class SpringexpressionlanguageApplicationTests {

    @Test//调用类属性
    void test1(){
        String exp="#user.username";
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(exp);
        User user = new User();
        user.setId(99);
        user.setUsername("javaboy");
        user.setAddress("深圳");
        //创建上下文环境，并设置值
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariable("user",user);
        String value = expression.getValue(standardEvaluationContext, String.class);
        System.out.println("=====>>>>value\n"+value);
    }
    @Test//调用类属性
    void test2(){
        String exp="username";
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(exp);
        User user = new User();
        user.setId(99);
        user.setUsername("javaboy");
        user.setAddress("深圳");
        //创建上下文环境，并设置值
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setRootObject(user);//用setRootObject就不用#user.
        String value = expression.getValue(standardEvaluationContext, String.class);
        System.out.println("=====>>>>value\n"+value);
    }

    //下面的test3 test4 同理也可以有带 #的形式（"#us.sayhello";UserService us=new UserService;）。略去
    @Test//调用方法,不带参数
    void test3(){
        //设置调用的内容
        String exp="sayHello";
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(exp);
        //创建上下文环境，并设置值
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setRootObject(new UserService());
        //从哪个上下文环境中调用
        String value = expression.getValue(standardEvaluationContext, String.class);
        System.out.println("========>\n"+value);
    }
    @Test
    void test4(){//带参数的函数调用
        //设置调用的内容
        String exp="sayHello(\" param-javaboy \")";//参数是字符串类型的 "params-javaboy"。此处转义是因为如果不转义，整个exp会被截断成两个字符串
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(exp);
        //创建上下文环境，并设置值
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setRootObject(new UserService());
        //从哪个上下文环境中调用
        String value = expression.getValue(standardEvaluationContext, String.class);
        System.out.println("========>\n"+value);
    }
    @Test//字符串变代码
    void contextLoads() {
        //类似于eval函数:可以执行一个表达式
        String exp1="1+2";
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        Expression expression = spelExpressionParser.parseExpression(exp1);
        Object value = expression.getValue();
        System.out.println("========>>>> value="+value);
    }

    /*切入正题
    * 甚至可以使用Bean*/
    @Autowired
    BeanFactory beanFactory;
    @Test
    void testBean(){
        String exp="@bs.showBook()"; //@表示Bean
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(exp);
        //上下文
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setBeanResolver(new BeanFactoryResolver(beanFactory));
        String value = expression.getValue(ctx, String.class);
        System.out.println("======>\n"+value);
    }
}
