package com.sdocean.dataQuery.model;

import java.util.List;

import com.sdocean.common.model.HchartsLineData;

public class StatisReportLineData {

	private List<HchartsLineData> datas;
	private String fieldName;
	
	public List<HchartsLineData> getDatas() {
		return datas;
	}
	public void setDatas(List<HchartsLineData> datas) {
		this.datas = datas;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
