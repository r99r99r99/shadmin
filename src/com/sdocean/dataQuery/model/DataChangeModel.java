package com.sdocean.dataQuery.model;

import com.sdocean.device.model.DeviceModel;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.page.model.PageResult;
import com.sdocean.station.model.StationModel;

public class DataChangeModel {
	
	private int id;
	private int stationId;
	private String stationName;
	private StationModel station;
	private int deviceId;
	private String deviceName;
	private DeviceModel device;
	private String indicatorCode;
	private String indicatorName;
	private IndicatorModel indicator;
	private String indicatorIds;
	private int indicatorId;
	private PageResult page;
	private String collect_time;     //采集时间
	private double newData;
	private double oldData;
	private String beginDate;
	private String endDate;
	private int collect_type;        //采集类型
	private String sensor_type_code;
	private String mark_code;
	private String createTime;
	private String creator;
	private String dataversion;
	private int unitId;
	private String unitName;
	private int changeType;      //修改类型  0是新增 1是修改 2是删除
	private String changeTypeName;
	private String changeTime;	 //修改时间
	private int userId;
	private String userName;	 //修改人员
	
	public int getIndicatorId() {
		return indicatorId;
	}
	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
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
	public int getCollect_type() {
		return collect_type;
	}
	public void setCollect_type(int collect_type) {
		this.collect_type = collect_type;
	}
	public String getSensor_type_code() {
		return sensor_type_code;
	}
	public void setSensor_type_code(String sensor_type_code) {
		this.sensor_type_code = sensor_type_code;
	}
	public String getMark_code() {
		return mark_code;
	}
	public void setMark_code(String mark_code) {
		this.mark_code = mark_code;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDataversion() {
		return dataversion;
	}
	public void setDataversion(String dataversion) {
		this.dataversion = dataversion;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public int getChangeType() {
		return changeType;
	}
	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}
	public String getChangeTypeName() {
		return changeTypeName;
	}
	public void setChangeTypeName(String changeTypeName) {
		this.changeTypeName = changeTypeName;
	}
	public String getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(String changeTime) {
		this.changeTime = changeTime;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public StationModel getStation() {
		return station;
	}
	public void setStation(StationModel station) {
		this.station = station;
	}
	public DeviceModel getDevice() {
		return device;
	}
	public void setDevice(DeviceModel device) {
		this.device = device;
	}
	public IndicatorModel getIndicator() {
		return indicator;
	}
	public void setIndicator(IndicatorModel indicator) {
		this.indicator = indicator;
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
	public PageResult getPage() {
		return page;
	}
	public void setPage(PageResult page) {
		this.page = page;
	}
	public String getIndicatorIds() {
		return indicatorIds;
	}
	public void setIndicatorIds(String indicatorIds) {
		this.indicatorIds = indicatorIds;
	}
	public int getStationId() {
		return stationId;
	}
	public void setStationId(int stationId) {
		this.stationId = stationId;
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
	public String getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(String collect_time) {
		this.collect_time = collect_time;
	}
	public double getNewData() {
		return newData;
	}
	public void setNewData(double newData) {
		this.newData = newData;
	}
	public double getOldData() {
		return oldData;
	}
	public void setOldData(double oldData) {
		this.oldData = oldData;
	}
}
