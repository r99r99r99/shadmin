package com.sdocean.metadata.model;

public class SyndataModel {

	private String collect_time;
	private String indicatorCode;
	private double data;
	private int wpid;
	private int collect_type;
	private int deviceId;
	
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	public double getData() {
		return data;
	}
	public void setData(double data) {
		this.data = data;
	}
	public int getWpid() {
		return wpid;
	}
	public void setWpid(int wpid) {
		this.wpid = wpid;
	}
	public int getCollect_type() {
		return collect_type;
	}
	public void setCollect_type(int collect_type) {
		this.collect_type = collect_type;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
}
