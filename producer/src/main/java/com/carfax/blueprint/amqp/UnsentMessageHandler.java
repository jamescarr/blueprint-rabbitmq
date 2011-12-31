package com.carfax.blueprint.amqp;

import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.util.Assert;

public class UnsentMessageHandler implements ConnectionListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(UnsentMessageHandler.class);
	private AmqpTemplate amqpTemplate;
	private Queue<UnsentMessage> failOverQueue;

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

	public void setFailOverQueue(Queue<UnsentMessage> failOverQueue) {
		this.failOverQueue = failOverQueue;
	}

	public void onCreate(Connection connection) {
		Assert.notNull(amqpTemplate, "amqpTemplate is required");
		Assert.notNull(failOverQueue, "failOverQueue is required");
		if (connection.isOpen() && !failOverQueue.isEmpty()) {
			UnsentMessage unsentMessage = null;
			LOG.info("Connection re-established. Publishing {} unsent messages!", failOverQueue.size());
			while ((unsentMessage = failOverQueue.poll()) != null) {
				amqpTemplate.send(unsentMessage.getExchange(),
						unsentMessage.getRoutingKey(),
						unsentMessage.getMessage());
			}
		}
	}

	public void onClose(Connection connection) {
		// no op
	}

}
