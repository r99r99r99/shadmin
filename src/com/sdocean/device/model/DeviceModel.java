package com.sdocean.device.model;

import java.util.List;

import com.sdocean.indicator.model.IndicatorModel;

public class DeviceModel {
	
	private int id;
	private String name;
	private String code;
	private String deviceModel;
	private String brief;
	private String detail;
	private String orderCode;
	private String indicatorIds;
	private String indicatorNames;
	private List<IndicatorModel> indicators;
	private Integer interval;
	
	
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public List<IndicatorModel> getIndicators() {
		return indicators;
	}
	public void setIndicators(List<IndicatorModel> indicators) {
		this.indicators = indicators;
	}
	public String getIndicatorIds() {
		return indicatorIds;
	}
	public void setIndicatorIds(String indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
	
	public String getIndicatorNames() {
		return indicatorNames;
	}
	public void setIndicatorNames(String indicatorNames) {
		this.indicatorNames = indicatorNames;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
}
