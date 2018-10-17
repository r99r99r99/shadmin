package com.sdocean.dataQuery.model;

import java.util.List;

import com.sdocean.common.model.SelectTree;
import com.sdocean.station.model.StationModel;

public class ManualDataModel {

	private int id;
	private int stationId;
	private String stationName;
	private String indicatorCode;
	private String indicatorName;
	private Double data;
	private String beginDate;
	private String endDate;
	private String collectTime;
	private String createTime;
	private int userId;
	private String userName;
	private List<StationModel> stations;
	private List<SelectTree> indicators;
	
	public Double getData() {
		return data;
	}
	public void setData(Double data) {
		this.data = data;
	}
	public List<SelectTree> getIndicators() {
		return indicators;
	}
	public void setIndicators(List<SelectTree> indicators) {
		this.indicators = indicators;
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
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
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
}
