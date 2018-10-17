package com.sdocean.warn.model;

public class RangeDataModel {

	private int id;
	private int stationId;
	private String stationName;
	private int deviceId;
	private String deviceName;
	private String indicatorCode;
	private String indicatorName;
	private Double minData;
	private Double maxData;
	
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
	public Double getMinData() {
		return minData;
	}
	public void setMinData(Double minData) {
		this.minData = minData;
	}
	public Double getMaxData() {
		return maxData;
	}
	public void setMaxData(Double maxData) {
		this.maxData = maxData;
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
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
}
