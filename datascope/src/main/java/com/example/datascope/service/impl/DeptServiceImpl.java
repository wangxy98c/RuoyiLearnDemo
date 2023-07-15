package com.example.datascope.service.impl;

import com.example.datascope.annotation.DataScope;
import com.example.datascope.entity.Dept;
import com.example.datascope.mapper.DeptMapper;
import com.example.datascope.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
    @Autowired
    DeptMapper deptMapper;
    @Override
    @DataScope(deptAlias = "d")//d 是sql语句里的别名，在mapper里写死的
    public List<Dept> getAllDepts(Dept dept) {
        return deptMapper.getAllDepts(dept);
    }
}
