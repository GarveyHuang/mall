package com.shura.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Garvey
 * @Created: 2021/10/21
 * @Description:
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.shura.mall.mapper", "com.shura.mall.dao"})
public class MyBatisConfig {
}
