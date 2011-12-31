package com.carfax.blueprint.amqp;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
@RunWith(MockitoJUnitRunner.class)
public class UnsentMessageHandlerTest {
	@Mock AmqpTemplate amqpTemplate;
	@Mock Connection connection;
	Queue<UnsentMessage> unsentMessages = new ConcurrentLinkedQueue<UnsentMessage>();
	private UnsentMessageHandler unsentMessageListener;
	
	@Before
	public void beforeEach(){
		unsentMessageListener = new UnsentMessageHandler();
		unsentMessageListener.setAmqpTemplate(amqpTemplate);
		unsentMessageListener.setFailOverQueue(unsentMessages);
	}
	@Test
	public void shouldDoNothingWhenQueueIsEmpty() {
		given(connection.isOpen()).willReturn(true);
		
		unsentMessageListener.onCreate(connection);
		
		verify(amqpTemplate, never()).send(anyString(), anyString(), any(Message.class));
	}
	
	@Test
	public void drainAnyMessagesInTheQueue(){
		Message message = new Message("one".getBytes(), null);
		Message message2 = new Message("two".getBytes(), null);
		given(connection.isOpen()).willReturn(true);
		unsentMessages.add(new UnsentMessage("foo.bar.baz", "awesome", message));
		unsentMessages.add(new UnsentMessage("foo.bar.baz", "awesome", message2));
		
		unsentMessageListener.onCreate(connection);
		
		assertTrue(unsentMessages.isEmpty());
		verify(amqpTemplate).send("awesome", "foo.bar.baz", message);
		verify(amqpTemplate).send("awesome", "foo.bar.baz", message2);
	}

}
