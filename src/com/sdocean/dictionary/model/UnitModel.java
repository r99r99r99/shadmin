package com.sdocean.dictionary.model;

public class UnitModel {
	
	private int id;
	private String code;
	private int pid;
	private int groupId;
	private String groupName;
	private String name;
	private String description;
	private String logo;
	private int isactive;
	private String isactiveName;
	private Double multiply;
	private String standardCode;
	
	public String getStandardCode() {
		return standardCode;
	}
	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}
	public Double getMultiply() {
		return multiply;
	}
	public void setMultiply(Double multiply) {
		this.multiply = multiply;
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
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
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
}
