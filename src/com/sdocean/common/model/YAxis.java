package com.sdocean.common.model;

import java.util.LinkedList;
import java.util.List;

public class YAxis {
	private static final long serialVersionUID = 1L;
	public String fieldName;
	public String unit;
	public List yAxis2 = new LinkedList();//具体值
	public List<PlotLine> plotLines;
	
	public List<PlotLine> getPlotLines() {
		return plotLines;
	}
	public void setPlotLines(List<PlotLine> plotLines) {
		this.plotLines = plotLines;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public List getyAxis2() {
		return yAxis2;
	}
	public void setyAxis2(List yAxis2) {
		this.yAxis2 = yAxis2;
	}
	
}
