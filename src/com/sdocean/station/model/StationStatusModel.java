package com.sdocean.station.model;

public class StationStatusModel {

	private int stationId;
	private String stationName;
	private double latitude;  //维度
	private double longitude;  //经度
	private String detail;
	private String pic;
	private double ifConn;     //判断是否联通
	private String ifConnIcon;       //展示是否联通的图标
	private double distance;
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
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
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getIfConn() {
		return ifConn;
	}
	public void setIfConn(double ifConn) {
		this.ifConn = ifConn;
	}
	public String getIfConnIcon() {
		return ifConnIcon;
	}
	public void setIfConnIcon(String ifConnIcon) {
		this.ifConnIcon = ifConnIcon;
	}
}
