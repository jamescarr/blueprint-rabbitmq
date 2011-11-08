package com.carfax.blueprint.amqp;


import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

@Configuration
public class ApplicationConfig {
	@Bean
	ConnectionFactory amqpConnectionFactory(){
		return new CachingConnectionFactory();
	}
	@Bean
	Queue queue(){
		return new Queue("vehicle.changes");
	}
	@Bean
	RabbitAdmin rabbitAdmin(){
		return new RabbitAdmin(amqpConnectionFactory());
	}
	
	@Bean
	public SimpleMessageListenerContainer container(){
		SimpleMessageListenerContainer container= new SimpleMessageListenerContainer(amqpConnectionFactory());
		container.setQueueNames("vehicle.changes");
		container.setAutoStartup(true);
		container.setConcurrentConsumers(2);
		container.setMessageListener(messageListener());
		return container;
	}
	@Bean
	public MessageListener messageListener() {
		return new SimpleMessageListener();
	}
	public static void main(String... args){
		new AnnotationConfigApplicationContext(ApplicationConfig.class);
	}
}
