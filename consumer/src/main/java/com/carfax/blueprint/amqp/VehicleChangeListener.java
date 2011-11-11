package com.carfax.blueprint.amqp;

public interface VehicleChangeListener {

	public abstract void handleMessage(final Vehicle vehicle);

}