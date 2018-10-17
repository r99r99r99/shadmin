package com.sdocean.sms.model;

public class SmsLog {
	
	private int id;
	private int userId;
	private String userName;
	private String telephone;
	private String title;
	private String message;
	private String sendTime;
	private String createTime;
	private int ifSend;
	private String ifSendName;
	private String error;
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getIfSend() {
		return ifSend;
	}
	public void setIfSend(int ifSend) {
		this.ifSend = ifSend;
	}
	public String getIfSendName() {
		return ifSendName;
	}
	public void setIfSendName(String ifSendName) {
		this.ifSendName = ifSendName;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
}
