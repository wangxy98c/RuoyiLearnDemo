# Test里有测试用例
## 用到了乐观锁：
    利用revision的值进行限制，每更新一次+1。当下次更新的值对不上，说明从查询到更新期间有其他人操作更新了。这次更新失败
## 用户操作:
**操作的表是 ACT_ID_USER **
`IdentityService identityService`
1. `identityService.saveUser(user)`但不能改密码
2. `identityService.createUserQuery().userId("javaboy").singleResult();`查询：更新用户需要revision一致
3. `identityService.updateUserPassword(user);`改密码，且可以连同其他的一起改
4. `identityService.deleteUser("javaboy");`
5. `st<User> list = identityService.createNativeUserQuery().sql("select * from ACT_ID_USER where EMAIL_=#{email}")
			.parameter("email", "javaboy@qq.com").list();` 用自定义SQL语句来查询
## 用户组操作
**操作的表是 ACT_ID_GROUP**
1. `identityService.saveGroup(g);`
2. `identityService.deleteGroup("leader");` 它会有两个删除sql（因为要先删除组中的用户）
3. `identityService.createMembership(userId,groupId);` 建立用户与组的关系
    操作的表是：ACT_ID_MEMBERSHIP.
4. `identityService.createGroupQuery()` 查询。继续点出各种方法
5. `identityService.saveGroup(group);` 修改前先查询
6. 同理也有自定义sql
## 查询系统信息
**操作的表是 ACT_ID_PROPERTY**
1. `idmManagementService.getProperties();`//key:schema.version;value:6.7.2.0
2. `idmManagementService.getTableName(Group.class);`// 获得参数里的那个实体类的表名=>ACT_ID_GROUP