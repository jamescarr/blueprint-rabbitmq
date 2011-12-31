package com.carfax.blueprint.amqp;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class BufferedRabbitTemplate extends RabbitTemplate {
	
	private Queue<UnsentMessage> failOverQueue;


	@Override
	public void send(String exchange, String routingKey, Message message)
			throws AmqpException {
		try{
			super.send(exchange, routingKey, message);			
		}catch(AmqpConnectException e){
			failOverQueue.add(new UnsentMessage(routingKey, exchange, message));
		}
	}


	public void setFailoverQueue(Queue<UnsentMessage> queue) {
		failOverQueue = queue;
		
	}
	
}
