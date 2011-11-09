package com.carfax.blueprint.amqp;


import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:com/carfax/blueprint/amqp/jndi-context.xml")
public class ApplicationConfig {
	@Autowired
	ConnectionFactory amqpConnectionFactory;
	
	@Bean
	Queue queue(){
		return new Queue("vehicle.changes");
	}
	@Bean
	RabbitAdmin rabbitAdmin(){
		return new RabbitAdmin(amqpConnectionFactory);
	}
	
	@Bean
	public SimpleMessageListenerContainer container(){
		SimpleMessageListenerContainer container= new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueueNames("vehicle.changes");
		container.setAutoStartup(true);
		container.setConcurrentConsumers(2);
		container.setMessageListener(messageListener());
		return container;
	}
	@Bean
	public MessageListener messageListener() {
		return new MessageListenerAdapter(new VehicleChangeListener(), new JsonMessageConverter());
	}
}
