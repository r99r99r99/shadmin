package com.sdocean.warn.model;

import java.util.List;

import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.station.model.StationModel;

public class WarnValueModel {
	private int id;
	private int wpId;
	private String wpName;
	private int deviceId;
	private String deviceName;
	private String indicatorCode;
	private String indicatorName;
	private String beginDate;
	private String endDate;
	private int type;
	private String typeName;
	private Double value;
	private String collect_time;
	private int operate;  //0:未操作 1:已操作
	private String operateName;  
	private String flag;  //备注
	private String remark;
	private Double minConfig;  //保留配置中的最低下限
	private Double maxConfig;  //保留配置中的最高上限
	private String unitName;
	private String ids;
	//
	private List<StationModel> stationList;
	private List<PublicModel> typeList;
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public List<StationModel> getStationList() {
		return stationList;
	}
	public void setStationList(List<StationModel> stationList) {
		this.stationList = stationList;
	}
	public List<PublicModel> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<PublicModel> typeList) {
		this.typeList = typeList;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getWpName() {
		return wpName;
	}
	public void setWpName(String wpName) {
		this.wpName = wpName;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getIndicatorName() {
		return indicatorName;
	}
	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWpId() {
		return wpId;
	}
	public void setWpId(int wpId) {
		this.wpId = wpId;
	}
	public int getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
	public String getIndicatorCode() {
		return indicatorCode;
	}
	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public int getOperate() {
		return operate;
	}
	public void setOperate(int operate) {
		this.operate = operate;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getMinConfig() {
		return minConfig;
	}
	public void setMinConfig(Double minConfig) {
		this.minConfig = minConfig;
	}
	public Double getMaxConfig() {
		return maxConfig;
	}
	public void setMaxConfig(Double maxConfig) {
		this.maxConfig = maxConfig;
	}
	
}
