package com.DC2.UMLBusApp.BusinessObjects;

public class BusStop {

	private double busLat;
	private double busLon;
	private String busStopName;

	public BusStop(String busStopName, double busLat, double busLon) {
		super();
		this.busStopName = busStopName;
		this.busLat = busLat;
		this.busLon = busLon;
	}	
	
	public double getBusLat() {
		return busLat;
	}

	public void setBusLat(double busLat) {
		this.busLat = busLat;
	}

	public double getBusLon() {
		return busLon;
	}

	public void setBusLon(double busLon) {
		this.busLon = busLon;
	}

	public String getBusStopName() {
		return busStopName;
	}

	public void setBusStopName(String busStopName) {
		this.busStopName = busStopName;
	}
}
