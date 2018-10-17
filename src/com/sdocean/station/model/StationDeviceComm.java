package com.sdocean.station.model;

public class StationDeviceComm {
	//基础部分
	private int stationId;
	private String stationName;
	
	private int deviceId;
	private String deviceName;
	
	//读取数据部分
	private String protocalVersion; //数据版本
	private String templateFile;  //模板
	private String dataItem;      //读取数据集合
	
	//通信部分
	private int id;
	private String ip;
	private String port;
	private String cronExp;
	private String lastDataTime;
	private String functionName;
	private int writeTimeOut;
	private int readTimeOut;
	
	//生成元数据表
	private String lastMetaDate;
	
	//是否发送短信
	private int ifSms;
	private String ifSmsName;
	
	//数据监测间隔时间
	private int betweenTime;  //以分钟为单位
	
	
	public int getBetweenTime() {
		return betweenTime;
	}

	public void setBetweenTime(int betweenTime) {
		this.betweenTime = betweenTime;
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

	public int getIfSms() {
		return ifSms;
	}

	public void setIfSms(int ifSms) {
		this.ifSms = ifSms;
	}

	public String getIfSmsName() {
		return ifSmsName;
	}

	public void setIfSmsName(String ifSmsName) {
		this.ifSmsName = ifSmsName;
	}

	public String getProtocalVersion() {
		return protocalVersion;
	}

	public void setProtocalVersion(String protocalVersion) {
		this.protocalVersion = protocalVersion;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getDataItem() {
		return dataItem;
	}

	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLastDataTime() {
		return lastDataTime;
	}

	public void setLastDataTime(String lastDataTime) {
		this.lastDataTime = lastDataTime;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public int getWriteTimeOut() {
		return writeTimeOut;
	}

	public void setWriteTimeOut(int writeTimeOut) {
		this.writeTimeOut = writeTimeOut;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public String getLastMetaDate() {
		return lastMetaDate;
	}

	public void setLastMetaDate(String lastMetaDate) {
		this.lastMetaDate = lastMetaDate;
	}

	
}
