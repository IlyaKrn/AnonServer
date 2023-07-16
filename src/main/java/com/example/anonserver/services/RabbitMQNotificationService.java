package com.example.anonserver.services;

import com.example.anonserver.api.models.notifications.NotificationResponse;
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

    public void createNotificationQueue(String secret){
        Queue queue = new Queue(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{secret}", secret), true, false, false);
        Binding binding = new Binding(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{secret}", secret), Binding.DestinationType.QUEUE, RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NOTIFICATIONS_PATTERN.replace("{secret}", String.valueOf(secret)), null);
        admin.declareQueue(queue);
        admin.declareBinding(binding);
    }

    public void destroyNotificationQueue(String secret){
        admin.purgeQueue(RabbitMQConfig.NOTIFICATIONS_QUEUE_PATTERN.replace("{secret}", secret));
    }

    public void sendNotification(String secret, NotificationResponse response){
        createNotificationQueue(secret);
        template.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_NOTIFICATIONS_PATTERN.replace("{secret}", secret), response);
    }

}
