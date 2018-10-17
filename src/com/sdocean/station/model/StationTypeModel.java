package com.sdocean.station.model;

public class StationTypeModel {
	private int id;
	private String code;
	private String name;
	private String remark;
	private int isactive;
	private String iactiveName;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getIsactive() {
		return isactive;
	}
	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}
	public String getIactiveName() {
		return iactiveName;
	}
	public void setIactiveName(String iactiveName) {
		this.iactiveName = iactiveName;
	}
}
