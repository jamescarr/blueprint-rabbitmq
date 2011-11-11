package com.carfax.blueprint.amqp;


import org.aopalliance.aop.Advice;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

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
		container.setErrorHandler(errorHandler());
		container.setAdviceChain(new Advice[]{retryInterceptor()});
		container.setMessageListener(messageListener(new StolenRecordListener()));
		return container;
	}
	@Bean
	public Advice retryInterceptor(){
		StatefulRetryOperationsInterceptorFactoryBean retry = new StatefulRetryOperationsInterceptorFactoryBean();
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(new SimpleRetryPolicy());
	
		retry.setRetryOperations(retryTemplate);
		return retry.getObject();
	}
	
	private ErrorHandler errorHandler() {
		return new LoggingErrorHandler(LoggerFactory.getLogger("blueprint-rabbitmq-consumer"));
	}
	private MessageListener messageListener(VehicleChangeListener listener) {
		return new MessageListenerAdapter(listener, new JsonMessageConverter());
	}
}
