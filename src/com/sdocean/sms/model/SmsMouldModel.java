package com.sdocean.sms.model;

public class SmsMouldModel {
	private int id;
	private int stationId;
	private String stationName;
	private int type;
	private String typeName;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getMould() {
		return mould;
	}
	public void setMould(String mould) {
		this.mould = mould;
	}
	
}
