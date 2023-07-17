# 主要演示Aware
1. demo希望UserService知道自己的名字,实现了BeanNameAware。在demo启动式可以会输出
### Aware就是让感知某件事，想感知什么就实现什么接口就行了
1. 实现了BeanFactoryAware接口。能自动获取到BeanFactory对象。就能干很多事
```java
@Test
void contextLoads() {
    //方法1
    //UserService userService = BeanUtils.getBean("userService");
    //userService.sayHello();
    
    //方法2
    UserService bean = BeanUtils2.getBean("userService");
    bean.sayHello();
}
```
通过Bean的名字获取到对象并调用其中方法。多种方法。
# 存在的意义
可以在与Spring无关的地方获取Bean来使用其中方法
# 主要示例代码都在Test里
