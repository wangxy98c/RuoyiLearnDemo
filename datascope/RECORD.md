# 核心思想
mapper的SQL语句写好基础后，通过对不同data_scope的判断生成相应的条件限制（主要是 in select ）字符串拼接到SQL后面
来完成数据过滤。
**但是这种方法的的代码耦合度非常高，比如注解的值和sql语句中的别称直接相关**
# 准备
为了简化，用了[MyBatis-Plus,根据数据库快速生成实体类](https://baomidou.com)
添加依赖
```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-generator</artifactId>
    <version>3.5.3.1</version>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```
在测试里写“代码生成器”并运行  
**记得启动类加@MapperScan(basePackages = "com.example.datascope.mapper")**

# Security准备
entity实现UserDetils
Service实现UserDetilsService
另外：security5 默认需要加密。此处设置为不加密（在password字段前添加{noop}即可）
参见UserServiceImpl

# 正式开始
## 核心思路
定义一个BaseEntity，所有的entiy都继承自它。它具有Map<String,String> params  
之后所有的**追加的SQL** 都放到params里。
```mysql
select * from sys_dept where del_flag='0' and dept_id in (select rd.`dept_id` from sys_user_role ur,sys_role_dept rd where ur.`user_id`=2 and ur.`role_id`=rd.`role_id`)
```
可以发现，上面的语句中后面括号内`user_id=2`是写死的，实际查询时它需要动态改变
比如权限等级为3的。或者可以查询全部的则不需要后面的语句。  
**其实BaseEntity.params.datascope就是帮助生成 ·首个and后面·的那部分语句的**
## 定义注解
## 切面
    根据不同情况生成sql语句

Entity/User中新添加了List<Role> roles
数据库中没有该字段，需要添加注解 ` @TableField(exist= false)` 表示数据库中没有此字段
且get，set方法
同理 BaseEntity的params也需要添加这个（不出现在sql里）。其他entiy继承自它

