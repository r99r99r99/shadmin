package com.sdocean.metadata.model;

public class MetadataModel {
	private int id;
	private String collect_time;
	private int collect_type;
	private int wpId;
	private String indicator_code;
	private double  data;
	private String createTime;
	private String dataversion;
	private int deviceId;
	private String markCode;
	private String sensorTypeCode;
	private String source;
	
	private int isactive;
	private String isactiveName;
	private String remark;
	private String tableName;  //该条记录需要查询的表格
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getIsactive() {
		return isactive;
	}
	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}
	public String getIsactiveName() {
		return isactiveName;
	}
	public void setIsactiveName(String isactiveName) {
		this.isactiveName = isactiveName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSensorTypeCode() {
		return sensorTypeCode;
	}
	public void setSensorTypeCode(String sensorTypeCode) {
		this.sensorTypeCode = sensorTypeCode;
	}
	public String getMarkCode() {
		return markCode;
	}
	public void setMarkCode(String markCode) {
		this.markCode = markCode;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public int getCollect_type() {
		return collect_type;
	}
	public void setCollect_type(int collect_type) {
		this.collect_type = collect_type;
	}
	public int getWpId() {
		return wpId;
	}
	public void setWpId(int wpId) {
		this.wpId = wpId;
	}
	public String getIndicator_code() {
		return indicator_code;
	}
	public void setIndicator_code(String indicator_code) {
		this.indicator_code = indicator_code;
	}
	public double getData() {
		return data;
	}
	public void setData(double data) {
		this.data = data;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDataversion() {
		return dataversion;
	}
	public void setDataversion(String dataversion) {
		this.dataversion = dataversion;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	
}
