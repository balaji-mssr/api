package com.tfl.api.domain;

public class Prediction {
	
	private String lineName;
	private String timeToStation;
	private String expectedArrivalTime;
	private String stationName;
	private String platformName;
	private String platformCode;
	private String currentLocation;
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getTimeToStation() {
		return timeToStation;
	}
	public void setTimeToStation(String timeToStation) {
		this.timeToStation = timeToStation;
	}
	public String getExpectedArrivalTime() {
		return expectedArrivalTime;
	}
	public void setExpectedArrivalTime(String expectedArrivalTime) {
		this.expectedArrivalTime = expectedArrivalTime;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public String getPlatformCode() {
		return platformCode;
	}
	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}
	public String getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}
	@Override
	public String toString() {
		return "Prediction [lineName=" + lineName + ", timeToStation="
				+ timeToStation + ", expectedArrivalTime="
				+ expectedArrivalTime + ", stationName=" + stationName
				+ ", platformName=" + platformName + ", platformCode="
				+ platformCode + ", currentLocation=" + currentLocation + "]";
	}
	
	

}
