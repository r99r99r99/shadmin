package com.sdocean.dataQuery.model;

import java.util.List;

import com.sdocean.device.model.DeviceModel;

public class GraphModel {
	private String beginDate;
	private String endDate;
	private String indicatorIds;
	private List<DeviceModel> devices;
	
	public List<DeviceModel> getDevices() {
		return devices;
	}
	public void setDevices(List<DeviceModel> devices) {
		this.devices = devices;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIndicatorIds() {
		return indicatorIds;
	}
	public void setIndicatorIds(String indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
	
}
