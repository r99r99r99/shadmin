package com.sdocean.dataQuery.model;

public class StatData {
	private Integer value;
	private String name;
	private String xtime;
	private String ydata;
	private int statcount;
	private String firstThing;
	
	private String color;
	private Double y;
	private int standard_grade;
	
	 
	public int getStandard_grade() {
		return standard_grade;
	}
	public void setStandard_grade(int standard_grade) {
		this.standard_grade = standard_grade;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Double getY() {
		return y;
	}
	public void setY(Double y) {
		this.y = y;
	}
	public String getFirstThing() {
		return firstThing;
	}
	public void setFirstThing(String firstThing) {
		this.firstThing = firstThing;
	}
	public int getStatcount() {
		return statcount;
	}
	public void setStatcount(int statcount) {
		this.statcount = statcount;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXtime() {
		return xtime;
	}
	public void setXtime(String xtime) {
		this.xtime = xtime;
	}
	public String getYdata() {
		return ydata;
	}
	public void setYdata(String ydata) {
		this.ydata = ydata;
	}
}
