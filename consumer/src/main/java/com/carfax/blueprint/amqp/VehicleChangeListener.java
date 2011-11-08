package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VehicleChangeListener {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleChangeListener.class);
	public void handleMessage(final Vehicle vehicle){
		LOG.info("Received " + vehicle.toString());
	}
}
