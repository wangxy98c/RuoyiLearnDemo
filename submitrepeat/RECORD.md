### 首先解决一个问题，
Json请求读过一次后，再读会出现`getReader() has already been called for this request`
导致controller return 出错，为了解决此问题
1. 定义一个HttpServletRequest修饰者用于对JSON的特殊处理  
**过滤器Filter先执行，拦截器Interceptor后执行** 
2. 定义过滤器，判断请求是否是JSON形式并惊醒处理
   1. 拦截器拦截（JSON）以后，给Wrapper处理（如果不是JSON，直接doFilter往下走）  
      它实现了构造函数（用于初始化）并重写了InputStream和getReader  
      相当于用新的HttpServletRequest（实际是修饰者）代替原来的类。进行了处理
   2. 返回的结果再扔回FilterChain往下走：  
      FilterChain 接口的 doFilter 方法用于通知 Web 容器把请求交给 Filter 链中的下一个 Filter 去处理  
      如果当前调用此方法的 Filter 对象是Filter 链中的最后一个 Filter，那么将把请求交给目标 Servlet 程序去处理。
3. 在WebConfig中配置上拦截器（注册Bean）  
   SpringMVC 拦截器拦截 /* 和 /** 的区别(但似乎无关,/url/hello也不能用/* * )  
   /* ： 匹配一级，即 /add , /query 等  
   /** ： 匹配多级，即 /add , /add/user, /add/user/user… 等  
### 封装Redis，用起来方便一些
   用了范型
### 再来实现防止重复提交功能
1. 自定义注解@RepeatSubmit  
2. 拦截器Iterceptor解析注解并进行处理（此demo使用拦截器interceptor）(其实也可以用aop)
   1. 判断方法是否有RepeatSubmit注解
   2. 用redis判断是否是重复提交
   3. 其中使用了Wrapper来获取请求的Json信息（Body里的）以便存入Redis判断（参考第一部分）
      还有Header里的Authorization(postman请求使用bearerToken携带)

**Redis没有设置序列化方案，会使得Redis的key自动带上一些前缀。设置方法参考 <模块ratelimiter>**