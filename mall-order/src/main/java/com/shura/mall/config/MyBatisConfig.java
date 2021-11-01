package com.shura.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description:
 */
@Configuration
@MapperScan({"com.shura.mall.mapper", "com.shura.mall.portal.dao", "com.shura.mall.dao"})
public class MyBatisConfig {
}