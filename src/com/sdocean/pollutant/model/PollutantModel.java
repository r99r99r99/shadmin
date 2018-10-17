package com.sdocean.pollutant.model;

import java.util.List;

import com.sdocean.common.model.SelectTree;
import com.sdocean.device.model.DeviceModel;

public class PollutantModel {
	
	private int id;
	private String collectDate;
	private int wpId;
	private String wpName;
	private int deviceId;
	private String deviceName;
	private String indicatorCode;
	private String indicatorName;
	private Double avgData;
	private Double polluData;
	private String beginDate;
	private String endDate;
	private List<SelectTree> indicatorTree;
	private String idcicatorCods;
	private int type;
	private String typeName;
	private String indicatorIds;
	private List<DeviceModel> devices;
	private String unit;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIndicatorIds() {
		return indicatorIds;
	}
	public void setIndicatorIds(String indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
	public List<DeviceModel> getDevices() {
		return devices;
	}
	public void setDevices(List<DeviceModel> devices) {
		this.devices = devices;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIdcicatorCods() {
		return idcicatorCods;
	}
	public void setIdcicatorCods(String idcicatorCods) {
		this.idcicatorCods = idcicatorCods;
	}
	public List<SelectTree> getIndicatorTree() {
		return indicatorTree;
	}
	public void setIndicatorTree(List<SelectTree> indicatorTree) {
		this.indicatorTree = indicatorTree;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	public int getWpId() {
		return wpId;
	}
	public void setWpId(int wpId) {
		this.wpId = wpId;
	}
	public String getWpName() {
		return wpName;
	}
	public void setWpName(String wpName) {
		this.wpName = wpName;
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
	public Double getAvgData() {
		return avgData;
	}
	public void setAvgData(Double avgData) {
		this.avgData = avgData;
	}
	public Double getPolluData() {
		return polluData;
	}
	public void setPolluData(Double polluData) {
		this.polluData = polluData;
	}
}
