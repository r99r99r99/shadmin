package com.sdocean.firstpage.model;

import java.util.Date;
import java.util.List;

import com.sdocean.indicator.model.IndicatorModel;

public class LastMetaData {
	private String deviceName;
	private String code;
	private Integer deviceId;
	private Integer pointNum;
	private String lastTime;
	private List<MetaData4FirstPage> MetaDatas;
	private List<IndicatorModel> indicator;
	
	 
	public Integer getPointNum() {
		return pointNum;
	}
	public void setPointNum(Integer pointNum) {
		this.pointNum = pointNum;
	}
	public List<IndicatorModel> getIndicator() {
		return indicator;
	}
	public void setIndicator(List<IndicatorModel> indicator) {
		this.indicator = indicator;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public List<MetaData4FirstPage> getMetaDatas() {
		return MetaDatas;
	}
	public void setMetaDatas(List<MetaData4FirstPage> metaDatas) {
		MetaDatas = metaDatas;
	}
	
}
