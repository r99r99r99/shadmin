package com.sdocean.river.model;

import java.util.List;

import com.sdocean.station.model.StationModel;

/*
 * 流域管理
 */
public class RiverModel {

	private int id;
	private String name;
	private Double length;
	private String remark;  //备注信息
	private String orderCode;   //排序编码
	private List<StationModel> stations;
	private String stationIds;
	private String stationName;
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStationIds() {
		return stationIds;
	}
	public void setStationIds(String stationIds) {
		this.stationIds = stationIds;
	}
	public List<StationModel> getStations() {
		return stations;
	}
	public void setStations(List<StationModel> stations) {
		this.stations = stations;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getLength() {
		return length;
	}
	public void setLength(Double length) {
		this.length = length;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
