package com.sdocean.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.dao.StationTypeDao;
import com.sdocean.station.model.StationTypeModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StationTypeService {

	@Resource
	private StationTypeDao typeDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4TypeList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", true, "20%");
		UiColumn col2 = new UiColumn("code", "code", true, "20%");
		UiColumn col13 = new UiColumn("类型名称", "name", true, "20%");
		UiColumn col3 = new UiColumn("备注", "remark", true, "20%");
		UiColumn col4 = new UiColumn("isactive", "isactive", false, "0%");
		UiColumn col5 = new UiColumn("状态", "iactiveName", true, "20%");
		cols.add(col1);
		cols.add(col2);
		cols.add(col13);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		return cols;
	}
	
	/*
	 * 为站点类型展示结果查询结果集
	 */
	public List<StationTypeModel> getTypeList(StationTypeModel type){
		return typeDao.getTypeList(type);
	}
	
	/*
	 * 保存站点类型修改
	 */
	public Result saveTypeChange(StationTypeModel model){
		return typeDao.saveTypeChange(model);
	}
	
	/*
	 * 新增站点类型保存
	 */
	public Result saveNewType(StationTypeModel model){
		return typeDao.saveNewType(model);
	}
}
