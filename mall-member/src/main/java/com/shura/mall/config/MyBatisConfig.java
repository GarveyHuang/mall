package com.shura.mall.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: Garvey
 * @Created: 2021/10/13
 * @Description: MyBatis 相关配置
 */
@Configuration
@EnableTransactionManagement
@ComponentScan({"com.shura.mall.mapper", "com.shura.mall.portal.dao", "com.shura.mall.dao"})
public class MyBatisConfig {
}
