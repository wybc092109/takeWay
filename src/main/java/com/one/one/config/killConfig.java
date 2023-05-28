package com.one.one.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class killConfig {
    @Bean
    public DirectExchange fanoutExchange(){
        return new DirectExchange("kill");
    }
    @Bean
    public Queue queue1(){
        return new Queue("killQueue");
    }
//    @Bean
//    public Queue queue2(){
//        return new Queue("fanout.queue2");
//    }
    @Bean
    public Binding binding(Queue queue1, DirectExchange directExchange){
        return BindingBuilder.
                bind(queue1).
                to(directExchange).with("routingKey");
    }
//    @Bean
//    public Binding binding2(Queue queue2,DirectExchange directExchange){
//        return BindingBuilder.
//                bind(queue2).
//                to(directExchange).with();
//    }
}