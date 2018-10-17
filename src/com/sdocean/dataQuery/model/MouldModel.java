package com.sdocean.dataQuery.model;

public class MouldModel {
	
	private int id;
	private int stationId;
	private String stationName;
	private String mould;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getMould() {
		return mould;
	}
	public void setMould(String mould) {
		this.mould = mould;
	}
}
