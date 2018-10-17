package com.sdocean.menu.model;

import java.util.List;

public class SysMenu {
	
	private String code;
	private String pcode;
	private int type;          //是否节点
	private String typeName;   //是否节点
	private String name;       //菜单名称
	private String url;        //菜单链接
	private String picture;    //图片链接地址
	private int isactive;      //启用标志
	private int order;         //排序标志
	private int level;         //菜单级别
	private List<SysMenu> childMenu;
	private int isopen;
	private int iscurr;
	
	public int getIsopen() {
		return isopen;
	}
	public void setIsopen(int isopen) {
		this.isopen = isopen;
	}
	public int getIscurr() {
		return iscurr;
	}
	public void setIscurr(int iscurr) {
		this.iscurr = iscurr;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public List<SysMenu> getChildMenu() {
		return childMenu;
	}
	public void setChildMenu(List<SysMenu> childMenu) {
		this.childMenu = childMenu;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getIsactive() {
		return isactive;
	}
	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
}
