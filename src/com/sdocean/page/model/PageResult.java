package com.sdocean.page.model;

import java.util.List;

public class PageResult {
	private List  cols;
	private List<?> rows;
	
	
	public List getCols() {
		return cols;
	}
	public void setCols(List cols) {
		this.cols = cols;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
}
