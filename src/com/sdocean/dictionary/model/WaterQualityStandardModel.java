package com.sdocean.dictionary.model;

public class WaterQualityStandardModel {
	
	private int id;
	private String item;
	private String indicatorName;
	private int standardId;
	private String standardName;
	private Double min_value;
	private Double max_value;
	private String remarks;
	private int waterType;
	private String waterTypeName;
	private String ids;
	private int min;
	private String minKey;
	private int max;
	private String maxKey;
	
	
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public String getMinKey() {
		return minKey;
	}
	public void setMinKey(String minKey) {
		this.minKey = minKey;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public String getMaxKey() {
		return maxKey;
	}
	public void setMaxKey(String maxKey) {
		this.maxKey = maxKey;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public int getStandardId() {
		return standardId;
	}
	public void setStandardId(int standardId) {
		this.standardId = standardId;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public Double getMin_value() {
		return min_value;
	}
	public void setMin_value(Double min_value) {
		this.min_value = min_value;
	}
	public Double getMax_value() {
		return max_value;
	}
	public void setMax_value(Double max_value) {
		this.max_value = max_value;
	}
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getWaterType() {
		return waterType;
	}
	public void setWaterType(int waterType) {
		this.waterType = waterType;
	}
	public String getWaterTypeName() {
		return waterTypeName;
	}
	public void setWaterTypeName(String waterTypeName) {
		this.waterTypeName = waterTypeName;
	}
}
