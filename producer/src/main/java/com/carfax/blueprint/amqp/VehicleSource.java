package com.carfax.blueprint.amqp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.google.common.collect.Iterators;

public class VehicleSource {
	private final LinkedList<Vehicle> vehicles = new LinkedList<Vehicle>();
	private final Iterator<Vehicle> cyclingIterator = Iterators.cycle(vehicles);
	
	public void add(Vehicle vehicle){
		vehicles.add(vehicle);
	}
	public Vehicle next() {
		try{
			return cyclingIterator.next();	
		}catch(NoSuchElementException e){
			return null;
		}
	}

}
