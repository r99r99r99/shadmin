package com.sdocean.domain.model;

public class DomainForm {

	private int domainId;
	private String indicatorCode;
	private String dataText;
	
	public int getDomainId() {
		return domainId;
	}
	public void setDomainId(int domainId) {
		this.domainId = domainId;
	}
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	public String getDataText() {
		return dataText;
	}
	public void setDataText(String dataText) {
		this.dataText = dataText;
	}
}
