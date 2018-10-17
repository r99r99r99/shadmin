package com.sdocean.station.model;

public class StationPictureModel {

	private int id;
	private int stationId;
	private int type;
	private String typeName;
	private String modiName;  //修改后,保存再服务器中的文件名
	private String origName;  //用户上传时的原始文件名
	private int userId;  
	private String userName;
	private String createTime;  //图片上传时间
	private String remark;   //添加备注
	private String src;   //图片读取路径,  由读取方法 + filepath + modiName 组成
	
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
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
	
	public String getModiName() {
		return modiName;
	}
	public void setModiName(String modiName) {
		this.modiName = modiName;
	}
	public String getOrigName() {
		return origName;
	}
	public void setOrigName(String origName) {
		this.origName = origName;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
