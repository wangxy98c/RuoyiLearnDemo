package com.example.datascope;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Types;
import java.util.Collections;

@SpringBootTest
class DatascopeApplicationTests {

    @Test
    void contextLoads() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/test1?serverTimezone=Asia/Shanghai&useSSL=false", "root", "12345678")
                .globalConfig(builder -> {
                    builder.author("myself") // 设置作者
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/wangxy/code/RuoyiLearnDemo/datascope/src/main/java"); // 指定输出目录
                })
                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    if (typeCode == Types.SMALLINT) {
                        // 自定义类型转换
                        return DbColumnType.INTEGER;
                    }
                    return typeRegistry.getColumnType(metaInfo);

                }))
                .packageConfig(builder -> {
                    builder.parent("com.example.datascope") // 设置父包名
                            //.moduleName("system") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/Users/wangxy/code/RuoyiLearnDemo/datascope/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("sys_dept","sys_role","sys_user") // 设置那些需要生成表 的表名
                            .addTablePrefix("sys_"); // 设置过滤表前缀(不要sys前缀)
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
