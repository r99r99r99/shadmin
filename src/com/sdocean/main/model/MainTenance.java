package com.sdocean.main.model;

import java.util.List;

import com.sdocean.station.model.StationModel;

public class MainTenance {

	private int id;
	private int stationId;
	private String stationName;
	private int deviceId;
	private String deviceIds;
	private String deviceName;
	private String beginTime;
	private String endTime;
	private int state;
	private String stateName;
	private int userId;
	private String userName;
	private String collectTime;
	private int configId;
	private String configName;
	private String reason;
	private String result;
	private String lastMainTime;
	private String planTime;
	private int mainnum;         //例行维护时间间隔
	private String material;     //维护耗费的材料
	private List<StationModel> stations;
	private int mtype ;          //操作类型
	private String createTime;   //设备的安装时间
	private int madcId ;         //站点包含设备的表的id
	private int amcId;
	private String amcName;
	private String picRemoveIds;   //移除绑定的照片
	
	public String getPicRemoveIds() {
		return picRemoveIds;
	}
	public void setPicRemoveIds(String picRemoveIds) {
		this.picRemoveIds = picRemoveIds;
	}
	public int getAmcId() {
		return amcId;
	}
	public void setAmcId(int amcId) {
		this.amcId = amcId;
	}
	public String getAmcName() {
		return amcName;
	}
	public void setAmcName(String amcName) {
		this.amcName = amcName;
	}
	public int getMadcId() {
		return madcId;
	}
	public void setMadcId(int madcId) {
		this.madcId = madcId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getMtype() {
		return mtype;
	}
	public void setMtype(int mtype) {
		this.mtype = mtype;
	}
	public List<StationModel> getStations() {
		return stations;
	}
	public void setStations(List<StationModel> stations) {
		this.stations = stations;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public int getMainnum() {
		return mainnum;
	}
	public void setMainnum(int mainnum) {
		this.mainnum = mainnum;
	}
	public String getLastMainTime() {
		return lastMainTime;
	}
	public void setLastMainTime(String lastMainTime) {
		this.lastMainTime = lastMainTime;
	}
	public String getPlanTime() {
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	public String getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
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
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
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
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
