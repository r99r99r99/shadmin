package com.sdocean.page.model;

public class NgColumn {
	private String aggLabelFilter;   //过滤器使用的标签(货币,日期等等),如果无则为空
	private String cellClass;        //为表格添加一个CSS类
	private String cellFilter;       //为表格使用的过滤器
	private String cellTemplate;
	private String displayName;      //列名称
	private String editableCellTemplate;  //default : <input ng-class="'colt' + col.index" ng-input="COL_FIELD" />
	private String enableCellEdit;        //是否可编辑
	private String field;
	private String groupable;             //default: true    用户将该列分组
	private String headerCellTemplate;    //表头结构样式
	private String headerClass;           //表头结构样式
	private int maxWidth;
	private int minWidth;
	private String pinnable;              //default:true    列是否可以被固定在左边
	private String resizable;             //default:true    是否可以调整列的大小
	private String sortable;              //default:true    是否允许列排序
	private String sortFn;                //设置列的排序函数
	private Boolean visible;               //设置该列是否可见
	private String width;                    //设置该列的宽度
	
	public NgColumn() {
		super();
	}
	public NgColumn(String displayName, String field, Boolean visible, String width) {
		super();
		this.displayName = displayName;
		this.field = field;
		this.visible = visible;
		this.width = width;
	}
	public NgColumn(String displayName, String field) {
		super();
		this.displayName = displayName;
		this.field = field;
	}
	public NgColumn(String displayName, String field, String width) {
		super();
		this.displayName = displayName;
		this.field = field;
		this.width = width;
	}
	public String getAggLabelFilter() {
		return aggLabelFilter;
	}
	public void setAggLabelFilter(String aggLabelFilter) {
		this.aggLabelFilter = aggLabelFilter;
	}
	public String getCellClass() {
		return cellClass;
	}
	public void setCellClass(String cellClass) {
		this.cellClass = cellClass;
	}
	public String getCellFilter() {
		return cellFilter;
	}
	public void setCellFilter(String cellFilter) {
		this.cellFilter = cellFilter;
	}
	public String getCellTemplate() {
		return cellTemplate;
	}
	public void setCellTemplate(String cellTemplate) {
		this.cellTemplate = cellTemplate;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEditableCellTemplate() {
		return editableCellTemplate;
	}
	public void setEditableCellTemplate(String editableCellTemplate) {
		this.editableCellTemplate = editableCellTemplate;
	}
	public String getEnableCellEdit() {
		return enableCellEdit;
	}
	public void setEnableCellEdit(String enableCellEdit) {
		this.enableCellEdit = enableCellEdit;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getGroupable() {
		return groupable;
	}
	public void setGroupable(String groupable) {
		this.groupable = groupable;
	}
	public String getHeaderCellTemplate() {
		return headerCellTemplate;
	}
	public void setHeaderCellTemplate(String headerCellTemplate) {
		this.headerCellTemplate = headerCellTemplate;
	}
	public String getHeaderClass() {
		return headerClass;
	}
	public void setHeaderClass(String headerClass) {
		this.headerClass = headerClass;
	}
	public int getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}
	public int getMinWidth() {
		return minWidth;
	}
	public void setMinWidth(int minWidth) {
		this.minWidth = minWidth;
	}
	public String getPinnable() {
		return pinnable;
	}
	public void setPinnable(String pinnable) {
		this.pinnable = pinnable;
	}
	public String getResizable() {
		return resizable;
	}
	public void setResizable(String resizable) {
		this.resizable = resizable;
	}
	public String getSortable() {
		return sortable;
	}
	public void setSortable(String sortable) {
		this.sortable = sortable;
	}
	public String getSortFn() {
		return sortFn;
	}
	public void setSortFn(String sortFn) {
		this.sortFn = sortFn;
	}
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	
}
