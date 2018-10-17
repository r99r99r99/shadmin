package com.sdocean.station.model;

import com.sdocean.common.model.SelectTree;

public class StationModel {
	private int id;
	private String code;
	private String title;
	private double latitude;  //维度
	private double longitude;  //经度
	private String station_gateway;  //站点地址
	private int stationtype_id;   //站点类型
	private String stationTypeName;  //站点类型姓名
	private int region_id;    //站点归属地区
	private String regionName;   //地区名称
	
	private String companyId;  //站点归属单位
	private String companyName;  //站点归属单位名称
	
	private String brief;         //站点描述
	private String detail;        //站点细节;
	private String pic;			  //站点图片
	private int isactive;         
	private String isactiveName;
	private int waterType;        //站点水的类型  sys_public 0005
	private String waterTypeName;
	private String orderCode;
	private SelectTree region;
	private String deviceIds;
	private String deviceNames;
	
	private int ifsms;
	private String ifsmsName;
	private String lastMetaDate;
	
	private String url;
	private String icon;
	
	private Double warndistance;
	private int standard;  //该站点的水质等级标准
	private String standardName; 
	
	// a.code,a.title,a.latitude,a.longitude,a.station_gateway,a.stationtype_id,a.dictregion_id,a.brief,a.detail
	// a.pic,a.isactive,a.waterType
	
	public Double getWarndistance() {
		return warndistance;
	}
	public String getStandardName() {
		return standardName;
	}
	public void setStandardName(String standardName) {
		this.standardName = standardName;
	}
	public int getStandard() {
		return standard;
	}
	public void setStandard(int standard) {
		this.standard = standard;
	}
	public void setWarndistance(Double warndistance) {
		this.warndistance = warndistance;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getId() {
		return id;
	}
	public String getLastMetaDate() {
		return lastMetaDate;
	}
	public void setLastMetaDate(String lastMetaDate) {
		this.lastMetaDate = lastMetaDate;
	}
	public int getIfsms() {
		return ifsms;
	}
	public void setIfsms(int ifsms) {
		this.ifsms = ifsms;
	}
	public String getIfsmsName() {
		return ifsmsName;
	}
	public void setIfsmsName(String ifsmsName) {
		this.ifsmsName = ifsmsName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getDeviceIds() {
		return deviceIds;
	}
	public void setDeviceIds(String deviceIds) {
		this.deviceIds = deviceIds;
	}
	public String getDeviceNames() {
		return deviceNames;
	}
	public void setDeviceNames(String deviceNames) {
		this.deviceNames = deviceNames;
	}
	public SelectTree getRegion() {
		return region;
	}
	public void setRegion(SelectTree region) {
		this.region = region;
	}
	public String getStationTypeName() {
		return stationTypeName;
	}
	public void setStationTypeName(String stationTypeName) {
		this.stationTypeName = stationTypeName;
	}
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getStation_gateway() {
		return station_gateway;
	}
	public void setStation_gateway(String station_gateway) {
		this.station_gateway = station_gateway;
	}
	public int getStationtype_id() {
		return stationtype_id;
	}
	public void setStationtype_id(int stationtype_id) {
		this.stationtype_id = stationtype_id;
	}
	
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public int getIsactive() {
		return isactive;
	}
	public void setIsactive(int isactive) {
		this.isactive = isactive;
	}
	public String getIsactiveName() {
		return isactiveName;
	}
	public void setIsactiveName(String isactiveName) {
		this.isactiveName = isactiveName;
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
