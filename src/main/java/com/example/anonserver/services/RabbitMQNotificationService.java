package com.example.anonserver.services;

import com.example.anonserver.configs.RabbitMQConfig;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQNotificationService {

    @Autowired
    private AmqpAdmin admin;
    @Autowired
    private RabbitTemplate template;

    public void createNotificationQueue(String username){
        Queue queue = new Queue(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{username}", String.valueOf(username)), true, false, false);
        Binding binding = new Binding(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{username}", String.valueOf(username)), Binding.DestinationType.QUEUE, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NOTIFICATIONS_PATTERN.replace("{uid}", String.valueOf(username)), null);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
    }

    public void destroyNotificationQueue(String username){
        admin.purgeQueue(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{username}", String.valueOf(username)));
    }

    public void sendNotification(String username, Object message){
        createNotificationQueue(username);
        template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NOTIFICATIONS_PATTERN.replace("{username}", String.valueOf(username)), message);
    }

}
