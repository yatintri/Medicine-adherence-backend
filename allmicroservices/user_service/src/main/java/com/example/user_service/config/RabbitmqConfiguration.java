package com.example.user_service.config;

import com.example.user_service.service.MailService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {

    @Value("${project.rabbitmq.queue}")
    private String queue_name;

    @Value("${project.rabbitmq.queue2}")
    private String queue2_name;


    @Value("${project.rabbitmq.exchange}")
    private String topic_exchange;

    @Value("${project.rabbitmq.routingkey}")
    private String routing_key;

    @Value("${project.rabbitmq.routingkey2}")
    private String routing_key2;




    @Bean(name = "queue1")
    public Queue getmailqueue(){
    return new Queue(queue_name);
}
//
    @Bean(name = "queue2")
    public Queue getnotificationqueue(){
        return new Queue(queue2_name);
    }


    @Bean
    public TopicExchange gettopicexchange(){
        return new TopicExchange(topic_exchange);
    }

    @Bean(name = "bind1")
    Binding binding1(@Qualifier("queue1") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routing_key);
    }

    @Bean(name = "bind2")
    Binding binding2(@Qualifier("queue2") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routing_key2);
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
