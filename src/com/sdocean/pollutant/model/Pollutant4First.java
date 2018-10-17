package com.sdocean.pollutant.model;

import java.util.List;

public class Pollutant4First {
	
	private int wpId;
	private String wpName;
	private String collectDate;
	private Double avgFlow;  //一天的平均流速
	private Double allFlow;  //一天的水通量
	private List<PollutantModel> polluList;
	
	public Double getAvgFlow() {
		return avgFlow;
	}
	public void setAvgFlow(Double avgFlow) {
		this.avgFlow = avgFlow;
	}
	public Double getAllFlow() {
		return allFlow;
	}
	public void setAllFlow(Double allFlow) {
		this.allFlow = allFlow;
	}
	public List<PollutantModel> getPolluList() {
		return polluList;
	}
	public void setPolluList(List<PollutantModel> polluList) {
		this.polluList = polluList;
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
	public String getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
}
