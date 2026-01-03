package com.mrakshith21.programming_judge.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubmissionQueueConfig {

    @Value("${rabbitmq.submission.topicName}")
    private String topicName;

    @Value("${rabbitmq.submission.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.submission.routingKey}")
    private String routingKey;

    @Bean
    public Queue myQueue() {
        return new Queue(topicName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
