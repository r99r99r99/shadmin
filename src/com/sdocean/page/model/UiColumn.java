package com.sdocean.page.model;

public class UiColumn {
	//http://ui-grid.info/docs/#/api/ui.grid.class:GridOptions.columnDef
	private Boolean aggregationHideLabel; //defaults to false, if set to true hides the label text in the aggregation footer, so only the value is displayed.
	private String aggregationType; //include uiGridConstants.aggregationTypes.count, uiGridConstants.aggregationTypes.sum, uiGridConstants.aggregationTypes.avg, uiGridConstants.aggregationTypes.min, uiGridConstants.aggregationTypes.max.
	private String cellClass;
	private String cellFilter;  // 该列的特征 'date:"yyyy-MM-dd"'  'address'
	private String cellTemplate;   //可以定义该列的功能按钮 '<button class="btn primary" ng-click="grid.appScope.showMe()">Click Me</button>'
	
	private Boolean cellTooltip;
	private Boolean enableCellEdit;   //将该列列为可编写
	private String type;              //该列的数据类型
	private String displayName;
	private Boolean enableColumnMenu;
	private Boolean enableColumnMenus;
	private Boolean enableFiltering;
	private Boolean enableHiding;
	private Boolean enableSorting;
	private String field;
	private String filter;
	private Boolean filterCellFiltered;
	private String filterHeaderTemplate;
	private String filters;
	private String footerCellClass;
	private String footerCellFilter;
	private String footerCellTemplate;
	private String headerCellClass;
	private String headerCellFilter;
	private String headerCellTemplate;
	private String headerTooltip;
	
	private Boolean pinnedRight;   //列固定靠右
	private Boolean pinnedLeft;    //列固定靠左
	private Boolean enableColumnResizing;  //是否允许该列改变大小
	
	private Integer maxWidth;
	private String menuItems;
	private Integer minWidth;
	private String name;
	private String sort;
	private String sortCellFiltered;
	private String sortDirectionCycle;
	private String sortingAlgorithm;
	private String suppressRemoveSort;
	private Boolean visible;
	private String width;
	
	public UiColumn() {
		super();
	}
	public UiColumn(String displayName, String field, Boolean visible,
			String width) {
		super();
		this.displayName = displayName;
		this.field = field;
		this.visible = visible;
		this.width = width;
	}
	
	public UiColumn(String displayName, String field,
			Boolean visible, String width,String cellFilter) {
		super();
		this.cellFilter = cellFilter;
		this.displayName = displayName;
		this.field = field;
		this.visible = visible;
		this.width = width;
	}
	public UiColumn(String displayName, String field, Boolean visible) {
		super();
		this.displayName = displayName;
		this.field = field;
		this.visible = visible;
	}
	
	
	public UiColumn( String displayName,String field, Boolean visible, String width,Boolean enableCellEdit, String type) {
		super();
		this.enableCellEdit = enableCellEdit;
		this.type = type;
		this.displayName = displayName;
		this.field = field;
		this.visible = visible;
		this.width = width;
	}
	public Boolean getEnableCellEdit() {
		return enableCellEdit;
	}
	public void setEnableCellEdit(Boolean enableCellEdit) {
		this.enableCellEdit = enableCellEdit;
	}
	public Boolean getPinnedRight() {
		return pinnedRight;
	}
	public void setPinnedRight(Boolean pinnedRight) {
		this.pinnedRight = pinnedRight;
	}
	public Boolean getPinnedLeft() {
		return pinnedLeft;
	}
	public void setPinnedLeft(Boolean pinnedLeft) {
		this.pinnedLeft = pinnedLeft;
	}
	public Boolean getEnableColumnResizing() {
		return enableColumnResizing;
	}
	public void setEnableColumnResizing(Boolean enableColumnResizing) {
		this.enableColumnResizing = enableColumnResizing;
	}
	public Boolean getAggregationHideLabel() {
		return aggregationHideLabel;
	}
	public void setAggregationHideLabel(Boolean aggregationHideLabel) {
		this.aggregationHideLabel = aggregationHideLabel;
	}
	public String getAggregationType() {
		return aggregationType;
	}
	public void setAggregationType(String aggregationType) {
		this.aggregationType = aggregationType;
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
	public Boolean getCellTooltip() {
		return cellTooltip;
	}
	public void setCellTooltip(Boolean cellTooltip) {
		this.cellTooltip = cellTooltip;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Boolean getEnableColumnMenu() {
		return enableColumnMenu;
	}
	public void setEnableColumnMenu(Boolean enableColumnMenu) {
		this.enableColumnMenu = enableColumnMenu;
	}
	public Boolean getEnableColumnMenus() {
		return enableColumnMenus;
	}
	public void setEnableColumnMenus(Boolean enableColumnMenus) {
		this.enableColumnMenus = enableColumnMenus;
	}
	public Boolean getEnableFiltering() {
		return enableFiltering;
	}
	public void setEnableFiltering(Boolean enableFiltering) {
		this.enableFiltering = enableFiltering;
	}
	public Boolean getEnableHiding() {
		return enableHiding;
	}
	public void setEnableHiding(Boolean enableHiding) {
		this.enableHiding = enableHiding;
	}
	public Boolean getEnableSorting() {
		return enableSorting;
	}
	public void setEnableSorting(Boolean enableSorting) {
		this.enableSorting = enableSorting;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public Boolean getFilterCellFiltered() {
		return filterCellFiltered;
	}
	public void setFilterCellFiltered(Boolean filterCellFiltered) {
		this.filterCellFiltered = filterCellFiltered;
	}
	public String getFilterHeaderTemplate() {
		return filterHeaderTemplate;
	}
	public void setFilterHeaderTemplate(String filterHeaderTemplate) {
		this.filterHeaderTemplate = filterHeaderTemplate;
	}
	public String getFilters() {
		return filters;
	}
	public void setFilters(String filters) {
		this.filters = filters;
	}
	public String getFooterCellClass() {
		return footerCellClass;
	}
	public void setFooterCellClass(String footerCellClass) {
		this.footerCellClass = footerCellClass;
	}
	public String getFooterCellFilter() {
		return footerCellFilter;
	}
	public void setFooterCellFilter(String footerCellFilter) {
		this.footerCellFilter = footerCellFilter;
	}
	public String getFooterCellTemplate() {
		return footerCellTemplate;
	}
	public void setFooterCellTemplate(String footerCellTemplate) {
		this.footerCellTemplate = footerCellTemplate;
	}
	public String getHeaderCellClass() {
		return headerCellClass;
	}
	public void setHeaderCellClass(String headerCellClass) {
		this.headerCellClass = headerCellClass;
	}
	public String getHeaderCellFilter() {
		return headerCellFilter;
	}
	public void setHeaderCellFilter(String headerCellFilter) {
		this.headerCellFilter = headerCellFilter;
	}
	public String getHeaderCellTemplate() {
		return headerCellTemplate;
	}
	public void setHeaderCellTemplate(String headerCellTemplate) {
		this.headerCellTemplate = headerCellTemplate;
	}
	public String getHeaderTooltip() {
		return headerTooltip;
	}
	public void setHeaderTooltip(String headerTooltip) {
		this.headerTooltip = headerTooltip;
	}
	public Integer getMaxWidth() {
		return maxWidth;
	}
	public void setMaxWidth(Integer maxWidth) {
		this.maxWidth = maxWidth;
	}
	public String getMenuItems() {
		return menuItems;
	}
	public void setMenuItems(String menuItems) {
		this.menuItems = menuItems;
	}
	public Integer getMinWidth() {
		return minWidth;
	}
	public void setMinWidth(Integer minWidth) {
		this.minWidth = minWidth;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getSortCellFiltered() {
		return sortCellFiltered;
	}
	public void setSortCellFiltered(String sortCellFiltered) {
		this.sortCellFiltered = sortCellFiltered;
	}
	public String getSortDirectionCycle() {
		return sortDirectionCycle;
	}
	public void setSortDirectionCycle(String sortDirectionCycle) {
		this.sortDirectionCycle = sortDirectionCycle;
	}
	public String getSortingAlgorithm() {
		return sortingAlgorithm;
	}
	public void setSortingAlgorithm(String sortingAlgorithm) {
		this.sortingAlgorithm = sortingAlgorithm;
	}
	public String getSuppressRemoveSort() {
		return suppressRemoveSort;
	}
	public void setSuppressRemoveSort(String suppressRemoveSort) {
		this.suppressRemoveSort = suppressRemoveSort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
