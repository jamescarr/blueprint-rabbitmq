package com.carfax.blueprint.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
	AmqpTemplate amqpTemplate(){
		RabbitTemplate template = new RabbitTemplate(amqpConnectionFactory());
		template.setRoutingKey("vehicle.changes");
		template.setMessageConverter(new JsonMessageConverter());
		return template;
	}
	
	@Bean(autowire=Autowire.BY_NAME)
	HistoryProcessor processor(){
		return new HistoryProcessor();
	}
	@Bean
	VehicleSource vehicleSource(){
		VehicleSource source = new VehicleSource();
		source.add(newVehicle("Toyota", "Tercel", "1995"));
		source.add(newVehicle("Ford", "Mustang", "2008"));
		source.add(newVehicle("Honda", "Prelude", "1985"));
		source.add(newVehicle("Honda", "Civic", "1999"));
		source.add(newVehicle("Nissan", "Altima", "2003"));
		return source;
	}
	
	@Bean
	BeanPostProcessor postProcessor(){
		return new ScheduledAnnotationBeanPostProcessor();
	}
	public static void main(String... args){
		new AnnotationConfigApplicationContext(ApplicationConfig.class);
	}
	private Vehicle newVehicle(String make, String model, String year) {
		Vehicle vehicle = new Vehicle();
		vehicle.setMake(make);
		vehicle.setModel(model);
		vehicle.setYear(year);
		return vehicle;
	}
}
