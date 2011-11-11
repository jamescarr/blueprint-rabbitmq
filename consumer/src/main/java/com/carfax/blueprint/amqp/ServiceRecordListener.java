package com.carfax.blueprint.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRecordListener implements VehicleChangeListener{
	private static final Logger LOG = LoggerFactory.getLogger(ServiceRecordListener.class);
	public void handleMessage(final Vehicle vehicle){
		LOG.info("Service Record: " + vehicle.toString());
	}
}
