package com.sdocean.warn.model;


public class WarnModel {
	
	private int id;
	private int wpId;
	private String wpName;
	private String indicatorCode;
	private String indicatorName;
	private Double warnLower;  //预警低值
	private Double warnUpper;  //预警高值
	private Double alarmLower; //告警低值
	private Double alarmUpper; //告警高值
	private String ids;
	private String collecttime;
	private String title;
	private String data;
	private String unitname;
	private String upper;
	private String lower;
	
	
	public String getCollecttime() {
		return collecttime;
	}
	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
	public String getUpper() {
		return upper;
	}
	public void setUpper(String upper) {
		this.upper = upper;
	}
	public String getLower() {
		return lower;
	}
	public void setLower(String lower) {
		this.lower = lower;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public Double getWarnLower() {
		return warnLower;
	}
	public void setWarnLower(Double warnLower) {
		this.warnLower = warnLower;
	}
	public Double getWarnUpper() {
		return warnUpper;
	}
	public void setWarnUpper(Double warnUpper) {
		this.warnUpper = warnUpper;
	}
	public Double getAlarmLower() {
		return alarmLower;
	}
	public void setAlarmLower(Double alarmLower) {
		this.alarmLower = alarmLower;
	}
	public Double getAlarmUpper() {
		return alarmUpper;
	}
	public void setAlarmUpper(Double alarmUpper) {
		this.alarmUpper = alarmUpper;
	}
}
