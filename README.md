> 提示：近期有时间打算升级和优化该项目。主要准备完成以下两个事情：
> 
> * 1、 微服务技术选型以 Spring Cloud Alibaba 为主。
> * 2、 优化项目分层，合并部分服务，降低服务整体的复杂性。


# 概述

基于微服务的思想，构建在 B2C 电商场景下的、前后端分离的商城项目。

目前项目整体功能比较简陋，项目划分比较简单，后续有时间会继续优化。

## 功能

目前，商城的整体功能如下图所示：

![整体功能](./document/images/商城整体功能.png)


# 技术

## 架构图

TODO 

## 项目结构

| 模块 | 名称  |
| ------ | ------ |
| mall-admin | 管理后台模块 |
| mall-auth-center | 权限中心 |
| mall-common | 公共模块 |
| mall-coupons | 优惠券模块 |
| mall-member | 会员模块 |
| mall-order | 订单模块 |
| mall-portal | 门户管理模块 |
| mall-product | 商品模块 |
| mall-search | 搜索模块 |
| mall-seckill | 秒杀模块 |
| mall-security | 安全认证模块 |
| mall-gateway | 网关 |

## 技术栈

### 后端
| 框架 | 说明  | 版本 |
| ------ | ------ | ------ |
| [Spring Boot](https://spring.io/projects/spring-boot) | Java 应用开发框架 | 2.1.17 |
| [MySQL](https://www.mysql.com/cn/) | 数据库 | 5.7 |
| [Druid](https://github.com/alibaba/druid) | JDBC 连接池、监控组件 | 1.1.17 |
| [MyBatis](http://www.mybatis.org/mybatis-3/zh/index.html) | 数据持久层框架 | 3.5.1 |
| [MyBatis-Plus](https://mp.baomidou.com/) | MyBatis 增强工具包 | 暂未引入 |
| [Redis](https://redis.io/) | kwy-value 数据库 | 5.0.3 |
| [Elasticsearch](https://www.elastic.co/cn/) | 分布式搜索引擎 | 7.6.1 |
| [RocketMQ](https://rocketmq.apache.org/) | 消息中间件 | 4.7.1 |
| [spring-cloud-gateway](https://spring.io/projects/spring-cloud-gateway) | 网关 | 2.3.2 |
| [springfox-swagger2](https://github.com/springfox/springfox/tree/master/springfox-swagger2) | API 文档 | 2.7.0 |

未来考虑引入
* [ ] 服务注册 / 服务发现 / 配置中心 Nacos
* [ ] 服务流控 Sentinel
* [ ] 分布式事务 Seta

### 监控

目前还没有引入监控功能，后续打算引入以下监控功能：

* [ ] 日志中心 ELK
* [ ] 监控告警 Prometheus
* [ ] 监控可视化 Grafana
* [ ] 调用链跟踪 SkyWalking

### 其它

* [ ] Jenkins 持续集成
* [ ] Nginx
* [ ] Docker 容器化部署
* [ ] Kubernetes 容器编排引擎