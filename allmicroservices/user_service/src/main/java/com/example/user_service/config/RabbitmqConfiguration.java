package com.example.user_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is config class for RabbitMQ message broker
 */
@Configuration
public class RabbitmqConfiguration {
    @Value("${project.rabbitmq.queue}")
    private String queueName;
    @Value("${project.rabbitmq.queueNotification}")
    private String queueNameNotification;
    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;
    @Value("${project.rabbitmq.routingKey}")
    private String routingKey;
    @Value("${project.rabbitmq.routingKeyNotification}")
    private String routingKeyNotification;

    @Bean(name = "bindMail")
    Binding bindingMail(@Qualifier("queueMail") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean(name = "bindNotification")
    Binding bindingNotification(@Qualifier("queueNotification") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKeyNotification);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(jsonMessageConverter());

        return rabbitTemplate;
    }

    @Bean(name = "queueMail")
    public Queue getMailQueue() {
        return new Queue(queueName);
    }

    @Bean(name = "queueNotification")
    public Queue getNotificationQueue() {
        return new Queue(queueNameNotification);
    }

    @Bean
    public TopicExchange getTopicExchange() {
        return new TopicExchange(topicExchange);
    }
}
