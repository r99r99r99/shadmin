package com.sdocean.dataQuery.model;

import com.sdocean.device.model.DeviceModel;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.page.model.PageResult;
import com.sdocean.station.model.StationModel;

public class DataCheckModel {

	private int stationId;
	private String indicatorIds;
	private String beginDate;
	private String endDate;
	private int isactive;
	private String remark;
	private StationModel station;
	private DeviceModel device;
	private IndicatorModel indicator;
	private PageResult page;
	private String inputString;
	
	public String getInputString() {
		return inputString;
	}
	public void setInputString(String inputString) {
		this.inputString = inputString;
	}
	public PageResult getPage() {
		return page;
	}
	public void setPage(PageResult page) {
		this.page = page;
	}
	public DeviceModel getDevice() {
		return device;
	}
	public void setDevice(DeviceModel device) {
		this.device = device;
	}
	public IndicatorModel getIndicator() {
		return indicator;
	}
	public void setIndicator(IndicatorModel indicator) {
		this.indicator = indicator;
	}
	public StationModel getStation() {
		return station;
	}
	public void setStation(StationModel station) {
		this.station = station;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
	}
	public String getIndicatorIds() {
		return indicatorIds;
	}
	public void setIndicatorIds(String indicatorIds) {
		this.indicatorIds = indicatorIds;
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
	public int getIsactive() {
		return isactive;
	}
	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
