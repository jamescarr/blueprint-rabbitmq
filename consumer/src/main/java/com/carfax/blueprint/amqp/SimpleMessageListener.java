package com.carfax.blueprint.amqp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class SimpleMessageListener implements MessageListener {

	public void onMessage(Message message) {
		System.out.println(new String(message.getBody()));
		
	}

}
