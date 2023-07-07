package com.example.datasources;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.sql.DataSource;
import java.util.Map;

@ConfigurationProperties(prefix = "spring.datasource")
public class DruidProperties {
    private String type;
    private String driverClassName;
    private Map<String,Map<String,String>> ds;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    /*在外部构造好一个DataSource对象，但它只包含url、username、passwaord三个核心属性
    * 在这个方法中，给这个对象设置其他的公共（通用的）属性，其实这些也可以不同自己配自己的。但一般没必要*/
    public DataSource dataSource(DruidDataSource druidDataSource){
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);//都一样，就统一设置了
        return druidDataSource;
    }//注意此处的DataSource不是注解的那个，而是sql里的
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Map<String, Map<String, String>> getDs() {
        return ds;
    }

    public void setDs(Map<String, Map<String, String>> ds) {
        this.ds = ds;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }
}
