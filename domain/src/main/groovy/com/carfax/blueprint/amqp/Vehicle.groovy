package com.carfax.blueprint.amqp

class Vehicle {
	String make, model, year
	String toString(){
		"[make:$make, model:$model, year:$year]"
	}
}
