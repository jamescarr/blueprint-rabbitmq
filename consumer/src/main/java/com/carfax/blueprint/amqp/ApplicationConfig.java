package com.carfax.blueprint.amqp;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
	Queue stolenQueue(){
		return new Queue("stolen_records");
	}
	@Bean
	Queue serviceQueue(){
		return new Queue("service_vehicles");
	}
	@Bean
	Binding stolenBinding(){
		return BindingBuilder
			.bind(stolenQueue())
			.to(new TopicExchange("vehicle_history_changes"))
			.with("vehicle.history.stolen");
	}
	@Bean
	Binding serviceBinding(){
		return BindingBuilder
			.bind(serviceQueue())
			.to(new TopicExchange("vehicle_history_changes"))
			.with("vehicle.history.service");
	}
	@Bean
	RabbitAdmin rabbitAdmin(){
		return new RabbitAdmin(amqpConnectionFactory);
	}
	@Bean
	public SimpleMessageListenerContainer serviceListener(){
		SimpleMessageListenerContainer container= new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueueNames(serviceQueue().getName());
		container.setAutoStartup(true);
		container.setConcurrentConsumers(2);
		container.setMessageListener(messageListener(new ServiceRecordListener()));
		return container;
	}
	@Bean
	public SimpleMessageListenerContainer stolenListener(){
		SimpleMessageListenerContainer container= new SimpleMessageListenerContainer(amqpConnectionFactory);
		container.setQueueNames(stolenQueue().getName());
		container.setAutoStartup(true);
		container.setConcurrentConsumers(2);
		container.setMessageListener(messageListener(new StolenRecordListener()));
		return container;
	}
	private MessageListener messageListener(VehicleChangeListener listener) {
		return new MessageListenerAdapter(listener, new JsonMessageConverter());
	}
}
