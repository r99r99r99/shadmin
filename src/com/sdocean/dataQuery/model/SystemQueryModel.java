package com.sdocean.dataQuery.model;

import java.util.List;
import java.util.Map;

import com.sdocean.common.model.SelectTree;
import com.sdocean.device.model.DeviceModel;


public class SystemQueryModel {
	
	private int stationId;
	private int deviceId;
	private String beginDate;
	private String endDate;
	
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
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
}
