package com.one.one.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient(){
        Config config=new Config();
        config.useSingleServer().setAddress("redis://192.168.233.112:6379");
        return Redisson.create(config);
    }
//    @Bean
//    public RedissonClient redissonClient2(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.184.129:6377");
//        return Redisson.create(config);
//    }
//    @Bean
//    public RedissonClient redissonClient3(){
//        Config config=new Config();
//        config.useSingleServer().setAddress("redis://192.168.184.129:6378");
//        return Redisson.create(config);
//    }
}