package com.shura.mall.config;

import com.shura.mall.component.lock.ZookeeperLock;
import com.shura.mall.component.lock.ZookeeperLockImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Garvey
 * @Created: 2021/10/16
 * @Description: zookeeper 配置
 */
@Configuration
public class ZookeeperCuratorConfig {

    @Value("${zookeeper.curator.retryCount}")
    private int retryCount;
    @Value("${zookeeper.curator.elapsedTimeMs}")
    private int elapsedTimeMs;
    @Value("${zookeeper.curator.connectUrl}")
    private String connectUrl;
    @Value("${zookeeper.curator.sessionTimeOutMs}")
    private int sessionTimeOutMs;
    @Value("${zookeeper.curator.connectionTimeOutMs}")
    private int connectionTimeOutMs;

    /**
     * 初始化客户端
     */
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                connectUrl,
                sessionTimeOutMs,
                connectionTimeOutMs,
                new RetryNTimes(retryCount, elapsedTimeMs));
    }

    @Bean
    public ZookeeperLock zookeeperLock() {
        return new ZookeeperLockImpl();
    }
}
