package com.sdocean.common.model;

import java.util.List;

public class Echarts {
	private static final long serialVersionUID = 1L;
	public List<Object> xAxis ;//多个折线图共有的横坐标
	public List<YAxis> yAxis;            //多个折线图的纵坐标
	public List<Object> getxAxis() {
		return xAxis;
	}
	public void setxAxis(List<Object> xAxis) {
		this.xAxis = xAxis;
	}
	public List<YAxis> getyAxis() {
		return yAxis;
	}
	public void setyAxis(List<YAxis> yAxis) {
		this.yAxis = yAxis;
	}
	
}
