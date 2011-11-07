package com.carfax.blueprint.amqp;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class VehicleSource {
	private final LinkedList<Vehicle> vehicles = new LinkedList<Vehicle>();
	public void add(Vehicle vehicle){
		vehicles.add(vehicle);
	}
	public Vehicle next() {
		try{
			return vehicles.pop();			
		}catch(NoSuchElementException e){
			return null;
		}
	}

}
