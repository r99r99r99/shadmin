package com.sdocean.common.model;

import java.util.List;

public class SelectTree {
	
	private String id;
	private String name;
	private Boolean selected;
	private List<SelectTree> children;
	private Boolean isExpanded;
	private Boolean isActive;
	
	
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Boolean getIsExpanded() {
		return isExpanded;
	}
	public void setIsExpanded(Boolean isExpanded) {
		this.isExpanded = isExpanded;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	public List<SelectTree> getChildren() {
		return children;
	}
	public void setChildren(List<SelectTree> children) {
		this.children = children;
	}
	
}
