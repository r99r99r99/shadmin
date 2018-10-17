package com.sdocean.domain.model;

public class DomainModel {

	private int id;
	private String code;
	private String name;
	private String remark;
	private int isactive;
	private String isactiveName;
	private String orderCode;
	private String stationIds;
	private String indicatorCodes;
	
	public String getStationIds() {
		return stationIds;
	}
	public void setStationIds(String stationIds) {
		this.stationIds = stationIds;
	}
	public String getIndicatorCodes() {
		return indicatorCodes;
	}
	public void setIndicatorCodes(String indicatorCodes) {
		this.indicatorCodes = indicatorCodes;
	}
	public String getIsactiveName() {
		return isactiveName;
	}
	public void setIsactiveName(String isactiveName) {
		this.isactiveName = isactiveName;
	}
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
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}
