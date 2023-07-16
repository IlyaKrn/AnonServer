package com.example.anonserver.configs;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "anon-exchange";
    public static final String NOTIFICATIONS_QUEUE_PATTERN = "notifications-{secret}";
    public static final String ROUTING_KEY_NOTIFICATIONS_PATTERN = "user.{secret}.notifications";


    @Bean
    public DirectExchange topicExchange(){
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }



}
