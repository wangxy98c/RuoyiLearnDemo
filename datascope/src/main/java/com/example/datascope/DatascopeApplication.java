package com.example.datascope;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.datascope.mapper")
public class DatascopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatascopeApplication.class, args);
    }

}
