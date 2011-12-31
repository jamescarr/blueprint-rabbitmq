package com.carfax.blueprint.amqp;

import java.io.Serializable;

import org.springframework.amqp.core.Message;

public class UnsentMessage implements Serializable{
	private static final long serialVersionUID = 712116599485842405L;
	private String routingKey;
	private String exchange;
	private Message message;
	
	public UnsentMessage(String routingKey, String exchange, Message message) {
		super();
		this.routingKey = routingKey;
		this.exchange = exchange;
		this.message = message;
	}
	public String getRoutingKey() {
		return routingKey;
	}
	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
}
