package com.sdocean.common.model;

import java.util.List;

public class HchartsServieModel {

	private Object result;   
	private List<double[]> data;
	
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public List<double[]> getData() {
		return data;
	}
	public void setData(List<double[]> data) {
		this.data = data;
	}
}
