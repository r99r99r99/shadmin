package com.sdocean.dataQuery.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sdocean.station.model.StationModel;

public class StatisModel {
	private String stationId;
	private int statType;
	private String beginDate;
	private String endDate;
	private List<StatData> datas;            //饼形图数据
	public List xtimes = new LinkedList();	 //横坐标数据
	public List<Object> ydatas = new ArrayList<Object>();  //纵坐标数据
	private String indicatorIds;
	private StationModel station;
	
	public StationModel getStation() {
		return station;
	}
	public void setStation(StationModel station) {
		this.station = station;
	}
	public String getIndicatorIds() {
		return indicatorIds;
	}
	public void setIndicatorIds(String indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
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
	public List<StatData> getDatas() {
		return datas;
	}
	public void setDatas(List<StatData> datas) {
		this.datas = datas;
	}
	public List getXtimes() {
		return xtimes;
	}
	public void setXtimes(List xtimes) {
		this.xtimes = xtimes;
	}
	public List<Object> getYdatas() {
		return ydatas;
	}
	public void setYdatas(List<Object> ydatas) {
		this.ydatas = ydatas;
	}
}
