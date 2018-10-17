package com.sdocean.frame.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class ExcelBean {

	private String sheetName; 
	
	private String fileName; 
	
	String[] title; 
	
	List<String[]> data;
	
	Integer[] columnWidth;
	
	public ExcelBean(){
		
	}
	
	public ExcelBean(String sheetName, String fileName, String[] title, List<String[]> data, Integer[] columnWidth){
		this.sheetName = sheetName;
		this.fileName = fileName;
		this.title = title;
		this.data = data;
		this.columnWidth = columnWidth;
	}
	
	public ExcelBean(String sheetName, String fileName, String[] title, List<String[]> data){
		this.sheetName = sheetName;
		this.fileName = fileName;
		this.title = title;
		this.data = data;
	}



	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getFileName() {
		try {
			return URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] getTitle() {
		return title;
	}

	public void setTitle(String[] title) {
		this.title = title;
	}

	public List<String[]> getData() {
		return data;
	}

	public void setData(List<String[]> data) {
		this.data = data;
	}

	public Integer[] getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(Integer[] columnWidth) {
		this.columnWidth = columnWidth;
	}
}
