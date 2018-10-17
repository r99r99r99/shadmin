package com.sdocean.main.model;

public class StationDeviceMainModel {
	
	private int id;
	private int madcId;
	private int stationId;
	private String stationName;
	private int deviceId;
	private String deviceName;
	private String createTime;
	private int amconfigId;
	private String mainConfigCode;
	private String mainConfigName;
	private String how;
	private int mainNum;
	
	public int getMainNum() {
		return mainNum;
	}
	public void setMainNum(int mainNum) {
		this.mainNum = mainNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMadcId() {
		return madcId;
	}
	public void setMadcId(int madcId) {
		this.madcId = madcId;
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
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getAmconfigId() {
		return amconfigId;
	}
	public void setAmconfigId(int amconfigId) {
		this.amconfigId = amconfigId;
	}
	public String getMainConfigCode() {
		return mainConfigCode;
	}
	public void setMainConfigCode(String mainConfigCode) {
		this.mainConfigCode = mainConfigCode;
	}
	public String getMainConfigName() {
		return mainConfigName;
	}
	public void setMainConfigName(String mainConfigName) {
		this.mainConfigName = mainConfigName;
	}
	public String getHow() {
		return how;
	}
	public void setHow(String how) {
		this.how = how;
	}
}
