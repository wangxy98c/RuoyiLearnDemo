package com.example.datasources;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
@Component
public class DynamicDataSource extends AbstractRoutingDataSource {
    public DynamicDataSource(LoadDataSource loadDataSource) {
        Map<String, DataSource> allDataSource = loadDataSource.loadAllDataSource();
        //设置所有的数据源,根据下面getDataSourceType返回的结果(数据源名)从这里边找
        super.setTargetDataSources(new HashMap<>(allDataSource));
        //设置默认的数据源(不是所有方法都有DataSource注解）
        super.setDefaultTargetDataSource(allDataSource.get("master"));//master作为默认数据源,也可以设置成常量
        //调用
    }

    /*这个方法返回数据源名称，当系统需要获取数据源时会自动调用该方法来获得
    * 拿到数据源名称后，会到Map中去*/
    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
}
