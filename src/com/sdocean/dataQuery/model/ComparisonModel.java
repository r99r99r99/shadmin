package com.sdocean.dataQuery.model;

import java.util.List;

import com.sdocean.common.model.PlotLine;
import com.sdocean.common.model.SelectTree;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.station.model.StationModel;

public class ComparisonModel {
	private String beginDate;
	private String endDate;
	private List<SelectTree> stationTree;
	private String stationIds;
	private List<StationModel> stations;
	private String indicatorCode;
	private IndicatorModel indicator;
	private List<ComparisonData> data;
	private List<PlotLine> plotLines;
	
	public List<PlotLine> getPlotLines() {
		return plotLines;
	}
	public void setPlotLines(List<PlotLine> plotLines) {
		this.plotLines = plotLines;
	}
	public IndicatorModel getIndicator() {
		return indicator;
	}
	public void setIndicator(IndicatorModel indicator) {
		this.indicator = indicator;
	}
	public List<ComparisonData> getData() {
		return data;
	}
	public void setData(List<ComparisonData> data) {
		this.data = data;
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
	public List<SelectTree> getStationTree() {
		return stationTree;
	}
	public void setStationTree(List<SelectTree> stationTree) {
		this.stationTree = stationTree;
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
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
}
