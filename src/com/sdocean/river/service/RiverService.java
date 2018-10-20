package com.sdocean.river.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.page.model.UiColumn;
import com.sdocean.river.dao.RiverDao;
import com.sdocean.river.model.RiverModel;
import com.sdocean.role.dao.RoleDao;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class RiverService {
	
	@Autowired
	RiverDao riverDao;
	
	/*
	 * 获得角色查询的表头
	 */
	public List<UiColumn> getCols4RiverList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("名称", "name", true, "*");
		UiColumn col3 = new UiColumn("流域长度(公里)", "length", true, "*");
		UiColumn col4 = new UiColumn("备注", "remark", false, "*");
		UiColumn col5 = new UiColumn("stationIds", "stationIds", false, "*");
		UiColumn col6 = new UiColumn("包含检测点", "stationName", true, "*");
		UiColumn col7 = new UiColumn("排序码", "orderCode", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		return cols;
	}

	/*
	 * 获得流域的列表
	 */
	public List<RiverModel> getRiverListByRiver(RiverModel model){
		return riverDao.getRiverListByRiver(model);
	}
	
	/*
	 * 新增流域
	 */
	public Result saveRiver(RiverModel model) {
		return riverDao.saveRiver(model);
	}
	
	/*
	 * 修改流域信息
	 */
	public Result updateRiver(RiverModel river) {
		return riverDao.updateRiver(river);
	}
	
	/*
	 * 获得流域中站点的列表
	 * 并选中当天流域有权限的站点
	 */
	public List<SelectTree> getRiverStationSelectTree(RiverModel river){
		return riverDao.getRiverStationSelectTree(river);
	}
}
