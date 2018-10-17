package com.sdocean.system.model;

public class SystemWarnModel {

	private String title;
	private String text;
	private int stationId;
	private String stationName;
	private int userId;
	private String userName;
	private String lastTime;
	private Double nowLatitude;//维度
	private Double nowLongitude; //经度
	
	public Double getNowLatitude() {
		return nowLatitude;
	}
	public void setNowLatitude(Double nowLatitude) {
		this.nowLatitude = nowLatitude;
	}
	public Double getNowLongitude() {
		return nowLongitude;
	}
	public void setNowLongitude(Double nowLongitude) {
		this.nowLongitude = nowLongitude;
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
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
