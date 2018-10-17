package com.sdocean.dataQuery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dataQuery.dao.SystemQueryDao;
import com.sdocean.dataQuery.model.DataChangeModel;
import com.sdocean.dataQuery.model.DataImportModel;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.SystemQueryModel;
import com.sdocean.firstpage.model.LastMetaData;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.metadata.model.MetadataModel;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class SystemQueryService {
	
	@Autowired
	private SystemQueryDao systemQueryDao;
	/*
	 * 为实时数据添加表头
	 */
	public List<UiColumn> getCols4SystemQuery(SystemQueryModel model){
		return systemQueryDao.getCols4SystemQuery(model);
	}
	
	/*
	 * 为实时数据查询提供结果
	 */
	public List<Map<String, Object>> getRows4SystemQuery(SystemQueryModel model){
		return systemQueryDao.getRows4SystemQuery(model);
	}
	
}
