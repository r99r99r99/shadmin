package com.sdocean.file.model;

public class FileModel {

	private String beforeName;  //保存前文件名称
	private String afterName;  //保存后文件名称
	private String configName;  //config文件中指定的路径
	private String filePath;   //文件保存路径 
	private String createTime;  //文件上传时间
	private Boolean  status;    //上传状态  true 代表上传成功 false 代表失败
	private String message;  
	
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBeforeName() {
		return beforeName;
	}
	public void setBeforeName(String beforeName) {
		this.beforeName = beforeName;
	}
	public String getAfterName() {
		return afterName;
	}
	public void setAfterName(String afterName) {
		this.afterName = afterName;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
