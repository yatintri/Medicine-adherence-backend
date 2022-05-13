package com.example.user_service.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    @Value("${project.rabbitmq.queue}")
    private String queueName;

    @Value("${project.rabbitmq.queue2}")
    private String queue2Name;


    @Value("${project.rabbitmq.exchange}")
    private String topicExchange;

    @Value("${project.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${project.rabbitmq.routingkey2}")
    private String routingKey2;




    @Bean(name = "queue1")
    public Queue getMailQueue(){
    return new Queue(queueName);
}
//
    @Bean(name = "queue2")
    public Queue getNotificationQueue(){
        return new Queue(queue2Name);
    }


    @Bean
    public TopicExchange getTopicExchange(){
        return new TopicExchange(topicExchange);
    }

    @Bean(name = "bind1")
    Binding binding1(@Qualifier("queue1") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean(name = "bind2")
    Binding binding2(@Qualifier("queue2") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey2);
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




}
//
