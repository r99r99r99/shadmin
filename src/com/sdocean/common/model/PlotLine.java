package com.sdocean.common.model;

public class PlotLine {
	
	private String color;   //标示线的颜色
	private String dashStyle;  //标示线的线条样式，默认是solid，即直线型，更多下面详细说明
	private int value;    //在坐标轴上显示的位置
	private int width;    //标示线的宽度
	private String text;   //标签的内容
	private String align;   //标签的水平位置，水平居左,默认是水平居中center
	private int x;          //标签相对于被定位的位置水平偏移的像素，重新定位
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDashStyle() {
		return dashStyle;
	}
	public void setDashStyle(String dashStyle) {
		this.dashStyle = dashStyle;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
}
