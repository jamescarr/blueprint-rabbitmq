package com.carfax.blueprint.amqp

import java.util.concurrent.atomic.AtomicInteger

import org.apache.commons.lang.builder.ToStringBuilder

class Vehicle {
	private static final AtomicInteger idSequence = new AtomicInteger(0);
	int id = idSequence.incrementAndGet()
	String make, model, year
	
	String toString(){
		ToStringBuilder.reflectionToString this
	}
}
