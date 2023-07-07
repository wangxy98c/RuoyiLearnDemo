package com.example.datasources;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@EnableConfigurationProperties(DruidProperties.class)
public class LoadDataSource {
    @Autowired
    DruidProperties druidProperties;
    public Map<String, DataSource> loadAllDataSource(){
        Map<String, DataSource> map=new HashMap<>();
        Map<String, Map<String, String>> ds = druidProperties.getDs();
        try {
            Set<String> keySet = ds.keySet();//keySet:返回每项的key值.即返回set[master,slave]
            for(String key: keySet){
                map.put(key, druidProperties.dataSource((DruidDataSource) DruidDataSourceFactory.createDataSource(ds.get(key))));
                //ds.get(key)拿到的是每个源里的url、username、password属性
                //DruidDataSourceFactory.createDataSource(ds.get(key)拿到的只有上面三项，
                // 需要用dataSource方法统一处理其他的配置，处理后再存入map
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}
