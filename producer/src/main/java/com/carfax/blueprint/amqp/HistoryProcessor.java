package com.carfax.blueprint.amqp;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class HistoryProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(HistoryProcessor.class);
	private final AtomicInteger count = new AtomicInteger(0);
	private AmqpTemplate amqpTemplate;
	private VehicleSource vehicleSource;
	public void setVehicleSource(VehicleSource vehicleSource) {
		this.vehicleSource = vehicleSource;
	}

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}
	
	@Scheduled(fixedRate=1000)
	public void process(){
		Vehicle vehicle = vehicleSource.next();
		if(vehicle != null){
			LOGGER.info("Sending vehicle with make " + vehicle.getMake() + " and model " + vehicle.getModel());
			if(count.incrementAndGet() % 3 == 0){
				amqpTemplate.convertAndSend("vehicle.history.stolen", vehicle);
			}else{
				amqpTemplate.convertAndSend("vehicle.history.service", vehicle);				
			}
		}
		
	}
}
