package com.sdocean.warn.model;

public class DeviceAlarmModel {

	private int id;
	private int stationId;
	private String stationName;
	private int deviceId;
	private String deviceName;
	private int configId;
	private String configName;
	private double alarmData;
	private String beginTime;
	private String endTime;
	private Double rangeData;
	private Double maxData;
	
	public Double getMaxData() {
		return maxData;
	}
	public void setMaxData(Double maxData) {
		this.maxData = maxData;
	}
	public Double getRangeData() {
		return rangeData;
	}
	public void setRangeData(Double rangeData) {
		this.rangeData = rangeData;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
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
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public double getAlarmData() {
		return alarmData;
	}
	public void setAlarmData(double alarmData) {
		this.alarmData = alarmData;
	}
}
