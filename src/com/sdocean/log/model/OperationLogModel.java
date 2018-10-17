package com.sdocean.log.model;

import java.util.Date;

public class OperationLogModel {
	
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	
	public static final int ADD = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
	
	private int id;
	private int userId;
	private String userName;
	private String menuname;
	private String url;
	private int dotype;
	private String dotypeName;
	private String model;
	private int result;
	private String resultName;
	private String message;
	private String errorcode;
	private Date operationtime;
	
	private String operaTime;
	private String beginTime;
	private String endTime;
	
	public String getResultName() {
		return resultName;
	}
	public void setResultName(String resultName) {
		this.resultName = resultName;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDotypeName() {
		return dotypeName;
	}
	public void setDotypeName(String dotypeName) {
		this.dotypeName = dotypeName;
	}
	public String getOperaTime() {
		return operaTime;
	}
	public void setOperaTime(String operaTime) {
		this.operaTime = operaTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getMenuname() {
		return menuname;
	}
	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDotype() {
		return dotype;
	}
	public void setDotype(int dotype) {
		this.dotype = dotype;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public Date getOperationtime() {
		return operationtime;
	}
	public void setOperationtime(Date operationtime) {
		this.operationtime = operationtime;
	}
	
}
