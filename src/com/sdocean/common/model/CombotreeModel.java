package com.sdocean.common.model;

import java.util.List;

public class CombotreeModel {
	
	public static final String OPEN = "open";
	public static final String CLOSED = "closed";
	
	private int id;          //绑定节点的标示值
	private String text;     //显示的节点文本
	private String state;    //节点状态 open 或者是closed
	private Boolean checked; //该节点是否被选中
	private String iconCls;  //显示的节点图标的CSS id
	private String attributes; //绑定该节点的自定义属性
	private String target;   //目标节点的DOM对象
	private List<CombotreeModel> children;
	
	public List<CombotreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<CombotreeModel> children) {
		this.children = children;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
}
