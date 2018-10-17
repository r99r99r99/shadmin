package com.sdocean.common.model;

public class Result {
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	/*
	 * 在公共代码中,parentcode = '0011'
	 */
	public static final int ADD = 1;
	public static final int UPDATE = 2;
	public static final int DELETE = 3;
	
	private int dotype;
	private int result;
	private String message;
	private String model;
	
	private Object res;
	
	
	public Object getRes() {
		return res;
	}
	public void setRes(Object res) {
		this.res = res;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public Result() {
		super();
	}
	public Result(int dotype) {
		super();
		this.dotype = dotype;
	}
	public int getDotype() {
		return dotype;
	}
	public void setDotype(int dotype) {
		this.dotype = dotype;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
