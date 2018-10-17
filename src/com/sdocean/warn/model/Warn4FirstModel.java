package com.sdocean.warn.model;

import java.util.List;

public class Warn4FirstModel {
	
	private String warnType;
	private List<WarnModel> warnModels;
	
	public String getWarnType() {
		return warnType;
	}
	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}
	public List<WarnModel> getWarnModels() {
		return warnModels;
	}
	public void setWarnModels(List<WarnModel> warnModels) {
		this.warnModels = warnModels;
	}
}
