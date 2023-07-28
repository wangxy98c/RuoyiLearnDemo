```java
//给用户设置角色
.roles("admin")
//设置两个权限
.authorities("system:user:add","sys:user:delete")
```
设置之后返回的是role还是authorities/permission呢？  
其实都会返回。因为如下源码
```java
for(int var5 = 0; var5 < var4; ++var5) {
    String role = var3[var5];
    Assert.isTrue(!role.startsWith("ROLE_"), () -> {
        return role + " cannot start with ROLE_ (it is automatically added)";
    });
    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
}
```
.role不能以ROLE_开头。他会自动添加前缀并添加到this.authorities。  
而.authorities也会添加到this.authorities
## Role和Authorities总结
**role和authorityes的区别仅仅是roles方法会自动添加`"ROLE_"`前缀。但其实它们都会存入authorities中。
但是：getAuthorityies时只会获取到authorities，因为会过滤掉ROLE_开头的**
按理说，此时我们使用PrePermission可以通过有权限的请求，但实际不能

#### 使用hasPermission时，我们需要自己实现权限评估器(implements PermissionEvaluator)
    但它可自定义（比如通配符）
#### 使用hasAuthority时，就不需要权限评估器了 。
    但它一般就不自定义匹配方式了。

Security中对权限和角色的判断代码没有什么大的区别，只是关于前缀而已。
最终都调用了hasAnyAuthorityName。区别在于第一个参数是null还是'ROLE_'而已

### RuoYi使用的是自己的Bean而不是在SecurityExpressionRoot基础上进行扩展
此demo是在Security的基础上进行扩展（使用自己定义的SecurityExpressionRoot里的方法），更符合常规做法。
1. 定义CustomSecurityExpressionRoot
2. 定义CustomMethodSecurityExpressionHandler（让Handler使用自己的CustomSecurityExpressionRoot。以及配置）
3. 在SecurityConfig中注册新的Handler

#### RuoYi改造的注册Bean不能写在这里SecurityConfig类中。因为这里的SecurityConfig只是一个普通的配置类。