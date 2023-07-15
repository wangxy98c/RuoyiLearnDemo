package com.example.datascope.aspect;

import com.example.datascope.annotation.DataScope;
import com.example.datascope.entity.BaseEntity;
import com.example.datascope.entity.Role;
import com.example.datascope.entity.User;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class DataScopeAspect {
    public static final String DATA_SCOPE_ALL="1";
    public static final String DATA_SCOPE_CUSTOM="2";
    public static final String DATA_SCOPE_DEPT="3";//只能自己部门
    public static final String DATA_SCOPE_DEPT_AND_CHILD="4";//自己部门以及下属部门
    public static final String DATA_SCOPE_SELF="5";//自己
    public static final String DATA_SCOPE="data_scope";//Map<String,String>的key

    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint jp, DataScope dataScope){
        //为防止Sql注入，先清除params
        clearDataScope(jp);
        //正式开始权限过滤
        //获取当前登陆用户信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserId()==1L){
            //超级管理员 ID=1 ,不需要权限过滤
            return;
        }
        StringBuilder sql = new StringBuilder();
        List<Role> roles = user.getRoles();
        for(Role role:roles){
            //每个角色都对应一小段sql的语句。由于可能有多个角色，语句之间应该用OR连接
            String ds=role.getDataScope();
            if(DATA_SCOPE_ALL.equals(ds)){
                //什么都能看，不用管
                return;
            } else if (DATA_SCOPE_CUSTOM.equals(ds)) {
                //自定义全新啊，根据用户角色查找部门id
                sql.append(String.format(" OR %s.dept_id in (select rd.dept_id from sys_role_dept rd where rd.role_id=%d)",dataScope.deptAlias(),role.getRoleId()));
            } else if (DATA_SCOPE_DEPT.equals(ds)) {
                //只能查看自己部门
                sql.append(String.format((" OR %s.dept_id=%d"),dataScope.deptAlias(),user.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(ds)) {
                //还能查看子部门
                sql.append(String.format(" OR %s.dept_id in (select dept_id from sys_dept where dept_id =%d or find_in_set(%d,ancestors))",dataScope.deptAlias(),user.getDeptId(),user.getDeptId()));
            } else if (DATA_SCOPE_SELF.equals(ds)) {
                //只能查看自己数据,其实就是不能查询部门数据
                String s = dataScope.userAlias();
                if("".equals(s)){//自己的信息都看不到
                    sql.append(" OR 1=0");
                }else{//能看到自己的
                    sql.append(String.format(" OR %s.user_id=%d",dataScope.userAlias(),user.getUserId()));
                }
            }
        }
        //生成的sql是or xxx or xxx or xxx，处理
        Object arg=jp.getArgs()[0];
        if(arg!=null && arg instanceof BaseEntity){
            BaseEntity baseEntity = (BaseEntity) arg;
            baseEntity.getParams().put(DATA_SCOPE,"AND("+sql.substring(4)+")");//" OR "前后各有各空格
        }
    }
    private void clearDataScope(JoinPoint jp){
        Object arg = jp.getArgs()[0];
        if (arg!=null && arg instanceof BaseEntity){
            BaseEntity baseEntity = (BaseEntity) arg;
            baseEntity.getParams().put(DATA_SCOPE,"");
        }
    }
}
