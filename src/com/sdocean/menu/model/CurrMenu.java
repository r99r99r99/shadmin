package com.sdocean.menu.model;

public class CurrMenu {
	
	private String pMenuId;
	private String pMenuName;
	private String cMenuId;
	private String cMenuName;
	private String curl;
	public String getCurl() {
		return curl;
	}
	public void setCurl(String curl) {
		this.curl = curl;
	}
	
	public String getpMenuName() {
		return pMenuName;
	}
	public void setpMenuName(String pMenuName) {
		this.pMenuName = pMenuName;
	}
	
	public String getpMenuId() {
		return pMenuId;
	}
	public void setpMenuId(String pMenuId) {
		this.pMenuId = pMenuId;
	}
	public String getcMenuId() {
		return cMenuId;
	}
	public void setcMenuId(String cMenuId) {
		this.cMenuId = cMenuId;
	}
	public String getcMenuName() {
		return cMenuName;
	}
	public void setcMenuName(String cMenuName) {
		this.cMenuName = cMenuName;
	}
	
}
