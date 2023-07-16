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

    public void createNotificationQueue(long secret){
        Queue queue = new Queue(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{secret}", String.valueOf(secret)), true, false, false);
        Binding binding = new Binding(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{secret}", String.valueOf(secret)), Binding.DestinationType.QUEUE, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NOTIFICATIONS_PATTERN.replace("{secret}", String.valueOf(secret)), null);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
    }

    public void destroyNotificationQueue(long secret){
        admin.purgeQueue(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{secret}", String.valueOf(secret)));
    }

    public void sendNotification(long secret, Object message){
        createNotificationQueue(secret);
        template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NOTIFICATIONS_PATTERN.replace("{secret}", String.valueOf(secret)), message);
    }

}
