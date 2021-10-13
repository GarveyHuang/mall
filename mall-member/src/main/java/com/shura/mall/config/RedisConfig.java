//package com.shura.mall.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
///**
// * @Author: Garvey
// * @Created: 2021/10/13
// * @Description: Redis 缓存相关配置
// */
//@Slf4j
//@Configuration
//public class RedisConfig {
//
//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//
//        // 设置 Connection
//        template.setConnectionFactory(factory);
//        // json 序列化
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        serializer.setObjectMapper(mapper);
//
//        // string 的 key/value 序列化方式
//        template.setKeySerializer(RedisSerializer.string());
//        template.setValueSerializer(serializer);
//
//        // hash 的 key/value 序列化方式
//        template.setHashKeySerializer(RedisSerializer.string());
//        template.setHashValueSerializer(serializer);
//
//        template.afterPropertiesSet();
//        return template;
//    }
//}
