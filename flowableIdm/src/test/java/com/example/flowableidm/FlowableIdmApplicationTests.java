package com.example.flowableidm;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmManagementService;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
class FlowableIdmApplicationTests {

	@Autowired
	IdentityService identityService;//负责用户相关的操作。例如添加/删除/修改 用户/用户组 等

	@Test
	void contextLoads(){
		//创建一个用户对象，需要设置属性
		UserEntityImpl user=new UserEntityImpl();
		user.setId("javaboy");
		user.setDisplayName("一点雨");
		user.setPassword("123");
		user.setFirstName("javaboy");
		user.setLastName("javaboy");
		user.setEmail("javaboy@qq.com");
		//Revision是配合乐观锁（用户表使用了）使用的
		user.setRevision(0);//添加用户要设置此属性，否则会被当作旧用户（更新用户）然后出错。没设置默认为1.可视为<版本号>
		identityService.saveUser(user);//此方法可以用来更新用户信息，但是不能更新密码。且每更新一次，数据库的Revision会自增1
		//自增1后会有如下影响：当一个更新不设置revision时默认为1.此时1<2。则会认为有其他实体在占用导致不能修改。=》即修改时需要确保revision和数据库中的<版本号>一样
	}
	@Test
	void test02(){
		//更新用户：为了解决<版本号>不一致,则需要查数据库（这里直接把用户全部查出来了)
		User user=identityService.createUserQuery().userId("javaboy").singleResult();
		user.setEmail("22@22.com");
		identityService.saveUser(user);
	}
	@Test
	void test03(){
		//更新用户：为了解决<版本号>不一致,则需要查数据库（这里直接把用户全部查出来了)
		User user=identityService.createUserQuery().userId("javaboy").singleResult();
		user.setEmail("333@333.com");
		user.setPassword("888");
		identityService.updateUserPassword(user);
	}
	@Test
	void test04(){
		identityService.deleteUser("javaboy");
	}
	private static final Logger logger= LoggerFactory.getLogger(FlowableIdmApplicationTests.class);
	@Test
	void test05(){
		//模糊查询
		List<User> list = identityService.createUserQuery().userDisplayNameLike("%一点%").list();
		for (User user : list) {
		}
	}
	@Test
	void test06(){
		List<User> list = identityService.createNativeUserQuery().sql("select * from ACT_ID_USER where EMAIL_=#{email}")
				.parameter("email", "javaboy@qq.com").list();
		for (User user : list) {
			System.out.println("====>"+user);
		}

	}
	@Test
	void test08(){
		//用户组
		GroupEntityImpl g=new GroupEntityImpl();
		g.setName("经理");
		g.setId("manager");
		g.setRevision(0);
		identityService.saveGroup(g);
	}
	@Test
	void test10(){
		//添加用户和用户组之间的关系
		String groupId="manager";
		String userId="javaboy";//它们使用了外键，需要确保参数真实存在。否则出错
		identityService.createMembership(userId,groupId);
	}
	@Test
	void test11(){
		//将manager用户组的name改为CEO。更新之前先查询（同理于用户）
		Group group = identityService.createGroupQuery().groupId("manager").singleResult();
		group.setName("CEO");
		identityService.saveGroup(group);
	}

	@Autowired
	IdmManagementService idmManagementService;
	@Test
	void test15(){
		//查询系统信息？
		Map<String, String> properties = idmManagementService.getProperties();
		Set<String> keySet = properties.keySet();
		for (String s : keySet) {
			logger.info("key:{};value:{}",s,properties.get(s));
		}
		//查询实体类所对应的表的名称
		String tableName1 = idmManagementService.getTableName(Group.class);
		String tableName2 = idmManagementService.getTableName(User.class);
		logger.info("tableName1:{};tableName2:{}",tableName1,tableName2);
	}
}
