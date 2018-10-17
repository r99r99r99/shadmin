package com.sdocean.region.model;


public class RegionModel {
	private int id;
	private int pid;
	private String code;
	private String text;
	private int level;
	
	@Override
	public boolean equals(Object obj) {
		RegionModel region = (RegionModel) obj;
		if(this.getId()==region.getId()){
			return true;
		}else{
			return false;
		}
	}
	
	public RegionModel() {
		super();
	}
	public RegionModel(int id, int pid, String code, String text, int level) {
		super();
		this.id = id;
		this.pid = pid;
		this.code = code;
		this.text = text;
		this.level = level;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
}
