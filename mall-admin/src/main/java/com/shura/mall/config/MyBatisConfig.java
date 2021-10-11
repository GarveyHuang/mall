package com.shura.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: Garvey
 * @date: 2021/10/11
 * @description: MyBatis 配置类
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.shura.mall.mapper", "com.shura.mall.dao"})
public class MyBatisConfig {
}
