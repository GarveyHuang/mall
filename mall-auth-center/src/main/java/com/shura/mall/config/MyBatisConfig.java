package com.shura.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garvey
 * @Created: 2021/10/20
 * @Description: MyBatis 配置
 */
@Configuration
@MapperScan({"com.shura.mall.mapper"})
public class MyBatisConfig {
}