package com.carfax.blueprint.amqp

import org.apache.commons.lang.builder.ToStringBuilder;

class Vehicle {
	String make, model, year
	
	String toString(){
		ToStringBuilder.reflectionToString this
	}
}
