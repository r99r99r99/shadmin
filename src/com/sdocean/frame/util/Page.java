package com.sdocean.frame.util;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class Page {
	protected int pageNo = 1;
	protected int pageSize = 1;
	@SuppressWarnings("rawtypes")
	protected List rows = Lists.newArrayList();
	protected long total = -1L;
	protected Map<String, String> Table;//表名
	protected Map<String, String> Fields;//字段名


	public Map<String, String> getTable() {
		return Table;
	}

	public void setTable(Map<String, String> table) {
		Table = table;
	}

	public Map<String, String> getFields() {
		return Fields;
	}

	public void setFields(Map<String, String> fields) {
		Fields = fields;
	}

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1)
			this.pageNo = 1;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1)
			this.pageSize = 1;
	}

	public int getBegin() {//oracle 起始行为1  mysql起始行为0
		int begin = (this.pageNo - 1) * this.pageSize;
		if(Constants.USE_DB_TYPE==1){
			begin+=1;
		}
		return begin;
	}

	@SuppressWarnings("rawtypes")
	public java.util.List getRows() {
		return rows;
	}

	@SuppressWarnings("rawtypes")
	public void setRows(java.util.List rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	
}