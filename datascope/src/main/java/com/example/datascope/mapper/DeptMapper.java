package com.example.datascope.mapper;

import com.example.datascope.entity.Dept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author myself
 * @since 2023-07-12
 */
public interface DeptMapper extends BaseMapper<Dept> {

    List<Dept> getAllDepts(Dept dept);
}
