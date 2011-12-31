package com.carfax.blueprint.amqp;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.Serializable;
import java.net.ConnectException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
@RunWith(MockitoJUnitRunner.class)
public class BufferedRabbitTemplateTest {
	@Mock(answer=Answers.RETURNS_DEEP_STUBS)
	ConnectionFactory connectionFactory;
	Queue<UnsentMessage> queue = new ConcurrentLinkedQueue<UnsentMessage>();
	private BufferedRabbitTemplate template = new BufferedRabbitTemplate();
	
	@Before
	public void beforeEach(){
		template.setConnectionFactory(connectionFactory);
		template.setFailoverQueue(queue);
		
		
	}
	@Test
	public void shouldPushMessageIntoQueueOnConnectionException() {
		given(connectionFactory.createConnection()).willThrow(new AmqpConnectException(new ConnectException("Connection Refused.")));
		
		template.convertAndSend("boom","mab.bam.boom", new Dude("James", "29"));

		UnsentMessage unsentMessage = queue.poll();
		Dude message = (Dude)new SimpleMessageConverter().fromMessage(unsentMessage.getMessage());
		
		assertThat(unsentMessage.getRoutingKey(), equalTo("mab.bam.boom"));
		assertThat(unsentMessage.getExchange(), equalTo("boom"));
		assertThat(message.getName(), equalTo("James"));
	}

	@Test
	public void shouldNotPopulateFailoverQueueWhenAnExceptionDoesNotOccur(){
		template.convertAndSend("boom","mab.bam.boom", new Dude("James", "29"));
		
		assertThat(queue.size(), equalTo(0));
	}
	
	public static class Dude implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name;
		private String age;
		
		public Dude() {
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAge() {
			return age;
		}
		public void setAge(String age) {
			this.age = age;
		}
		public Dude(String name, String age) {
			super();
			this.name = name;
			this.age = age;
		}
	}

}
