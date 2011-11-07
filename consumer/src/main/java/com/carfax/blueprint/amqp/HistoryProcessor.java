package com.carfax.blueprint.amqp;

import java.util.List;
import static java.util.Arrays.asList;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class HistoryProcessor {
	
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
			amqpTemplate.convertAndSend(vehicle);
		}
		
	}
}
