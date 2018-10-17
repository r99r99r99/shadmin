package com.sdocean.common.model;

import java.util.List;

import com.sdocean.company.model.CompanyModel;

public class ZTreeModel {
	
	private String id;
	private String pId;
	private String name;
	private String file;
	private Boolean open;
	private Integer ifNode;   //代表是否节点
	private String url;
	private String target;
	private Boolean checked;   //初始化默认节点被勾选
	private Boolean nocheck;   //设置某节点不显示checkbox
	private Boolean isParent;
	private String icon;
	private List<ZTreeModel> children;
	
	private String stationURL;
	private String deviceURL;
	private String deviceChildURL;
	
	private static String self = "_self";
	private static String iconPath = "/images/ztree/";
	
	@Override
	public boolean equals(Object obj) {
		ZTreeModel ztree = (ZTreeModel) obj;
		if(this.getId().equals(ztree.getId())){
			return true;
		}else{
			return false;
		}
		// TODO Auto-generated method stub
		//return super.equals(obj);
	}
	
	public ZTreeModel() {
		super();
	}
	public ZTreeModel(String id, String pId, String name, String file,
			Boolean open, Integer ifNode, String url, String target,
			Boolean checked, Boolean nocheck) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.file = file;
		this.open = open;
		this.ifNode = ifNode;
		this.url = url;
		this.target = target;
		this.checked = checked;
		this.nocheck = nocheck;
	}
	
	
	
	
	public String getStationURL() {
		return stationURL;
	}

	public void setStationURL(String stationURL) {
		this.stationURL = stationURL;
	}

	public String getDeviceURL() {
		return deviceURL;
	}

	public void setDeviceURL(String deviceURL) {
		this.deviceURL = deviceURL;
	}

	public String getDeviceChildURL() {
		return deviceChildURL;
	}

	public void setDeviceChildURL(String deviceChildURL) {
		this.deviceChildURL = deviceChildURL;
	}

	public static String getIconPath() {
		return iconPath;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static String getSelf() {
		return self;
	}
	public Boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
	public List<ZTreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<ZTreeModel> children) {
		this.children = children;
	}
	public Boolean getNocheck() {
		return nocheck;
	}
	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Integer getIfNode() {
		return ifNode;
	}
	public void setIfNode(Integer ifNode) {
		this.ifNode = ifNode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}
