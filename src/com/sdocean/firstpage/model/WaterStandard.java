package com.sdocean.firstpage.model;

import java.util.Date;

public class WaterStandard {
	
	private Integer standard_grade;
	private String standardName;
	private String fieldName;
	private String unit;
	private float sdata;
	private float mindata;
	private float maxdata;
	private String indicatorcode;
	private String indicatorName;
	private String xtime;
	private Integer value;
	private String levelValue;
	
	
	public String getLevelValue() {
		return levelValue;
	}
	public void setLevelValue(String levelValue) {
		this.levelValue = levelValue;
	}
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getStandard_grade() {
		return standard_grade;
	}
	public void setStandard_grade(Integer standard_grade) {
		this.standard_grade = standard_grade;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
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
	public float getSdata() {
		return sdata;
	}
	public void setSdata(float sdata) {
		this.sdata = sdata;
	}
	public float getMindata() {
		return mindata;
	}
	public void setMindata(float mindata) {
		this.mindata = mindata;
	}
	public float getMaxdata() {
		return maxdata;
	}
	public void setMaxdata(float maxdata) {
		this.maxdata = maxdata;
	}
	public String getIndicatorcode() {
		return indicatorcode;
	}
	public void setIndicatorcode(String indicatorcode) {
		this.indicatorcode = indicatorcode;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	
}
