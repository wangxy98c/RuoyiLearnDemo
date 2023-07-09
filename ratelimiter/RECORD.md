1. 定义一个枚举类（其实也可以用接口）表示常量
2. 定义注解
3. 配置一下Redis的序列化方案。（部分代码用到命令行的指令）
4. resources/lua 写lua脚本。
   也可以直接用Redis操作，但lua效率更高，且能保证原子性。
   Lua可参见notion-Redis笔记：https://wangxy98c.notion.site/NOSQL-Redis-27a5d9a91b0a43b8ad3bf67ccd7f7eb5?pvs=4
   笔记中都是在Redis服务端写好，java端调用。此处在java端写lua脚本
5. java配置脚本返回类型以及脚本路径。
6. 限流异常以及限流异常处理
6. 开始写 切面.
   1. 拼接字串获得key。调用脚本返回数字。
   2. 根据数字判断是否要限流
   3. 处理过程中的异常即可(限流也抛异常)




---
@ControllerAdvice  
@RestControllerAdvice
都是对Controller进行增强的，可以全局捕获spring mvc抛的异常。有这个注解的类中的方法的某些注解会应用到所有的Controller里
---
```java 
public interface JoinPoint {  
   String toString();         //连接点所在位置的相关信息  
   String toShortString();     //连接点所在位置的简短相关信息  
   String toLongString();     //连接点所在位置的全部相关信息  
   Object getThis();         //返回AOP代理对象  
   Object getTarget();       //返回目标对象  
   Object[] getArgs();       //返回被通知方法参数列表  
   Signature getSignature();  //返回当前连接点签名  
   SourceLocation getSourceLocation();//返回连接点方法所在类文件中的位置  
   String getKind();        //连接点类型  
   StaticPart getStaticPart(); //返回连接点静态部分  
  }  
 
 public interface ProceedingJoinPoint extends JoinPoint {  
       public Object proceed() throws Throwable;  
       public Object proceed(Object[] args) throws Throwable;  
 }  

```
---
