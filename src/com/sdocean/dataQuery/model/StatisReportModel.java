package com.sdocean.dataQuery.model;

import java.util.List;

import com.sdocean.common.model.PlotLine;
import com.sdocean.common.model.SelectTree;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.page.model.PageResult;
import com.sdocean.station.model.StationModel;

public class StatisReportModel {

	private int stationId;
	private StationModel station;         //站点信息
	private String indicatorIds;
	private String beginDate;
	private String endDate;
	private int collectType;              //统计口径
	private List<DeviceModel> devices;    //查询参数中包含的设备参数
	private PageResult pageResult;
	private List<SelectTree> indicatorTree;   
	private String statTypes;             //平均值\最小值-最小值时间\最大值-最大值时间\差值\
	private List<SelectTree> statTypeTree;
	private IndicatorModel indicator;   //获得查询的参数
	//定义返回结果
	private String collectTime;
	private Double avgdata;
	private Double maxdata;
	private String maxtime;
	private Double mindata;
	private String mintime;
	private Double diffdata;
	private Double amplidata;
	
	private List<StatisReportLineData> datas;   //定义折现图的返回结果
	private List<PlotLine> plotLines;			//定义预警告警提醒
	
	
	public IndicatorModel getIndicator() {
		return indicator;
	}
	public void setIndicator(IndicatorModel indicator) {
		this.indicator = indicator;
	}
	public List<PlotLine> getPlotLines() {
		return plotLines;
	}
	public void setPlotLines(List<PlotLine> plotLines) {
		this.plotLines = plotLines;
	}
	public List<StatisReportLineData> getDatas() {
		return datas;
	}
	public void setDatas(List<StatisReportLineData> datas) {
		this.datas = datas;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public Double getAvgdata() {
		return avgdata;
	}
	public void setAvgdata(Double avgdata) {
		this.avgdata = avgdata;
	}
	public Double getMaxdata() {
		return maxdata;
	}
	public void setMaxdata(Double maxdata) {
		this.maxdata = maxdata;
	}
	public String getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(String maxtime) {
		this.maxtime = maxtime;
	}
	public Double getMindata() {
		return mindata;
	}
	public void setMindata(Double mindata) {
		this.mindata = mindata;
	}
	public String getMintime() {
		return mintime;
	}
	public void setMintime(String mintime) {
		this.mintime = mintime;
	}
	public Double getDiffdata() {
		return diffdata;
	}
	public void setDiffdata(Double diffdata) {
		this.diffdata = diffdata;
	}
	public Double getAmplidata() {
		return amplidata;
	}
	public void setAmplidata(Double amplidata) {
		this.amplidata = amplidata;
	}
	public String getStatTypes() {
		return statTypes;
	}
	public void setStatTypes(String statTypes) {
		this.statTypes = statTypes;
	}
	public List<SelectTree> getStatTypeTree() {
		return statTypeTree;
	}
	public void setStatTypeTree(List<SelectTree> statTypeTree) {
		this.statTypeTree = statTypeTree;
	}
	public List<SelectTree> getIndicatorTree() {
		return indicatorTree;
	}
	public void setIndicatorTree(List<SelectTree> indicatorTree) {
		this.indicatorTree = indicatorTree;
	}
	public PageResult getPageResult() {
		return pageResult;
	}
	public void setPageResult(PageResult pageResult) {
		this.pageResult = pageResult;
	}
	public StationModel getStation() {
		return station;
	}
	public void setStation(StationModel station) {
		this.station = station;
	}
	public int getCollectType() {
		return collectType;
	}
	public void setCollectType(int collectType) {
		this.collectType = collectType;
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
	public List<DeviceModel> getDevices() {
		return devices;
	}
	public void setDevices(List<DeviceModel> devices) {
		this.devices = devices;
	}
}
