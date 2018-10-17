package com.sdocean.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.dao.StationDao;
import com.sdocean.station.dao.StationDeviceIndicatorDao;
import com.sdocean.station.model.StationDeviceIndicator;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StationDeviceIndicatorService {

	@Resource
	private StationDao stationDao;
	@Resource
	private StationDeviceIndicatorDao sdiDao;
	
	/*
	 * 根据站点,以ztree的形式展示有显示权限的设备以及关联的参数
	 */
	public List<ZTreeModel> getDeiviceIndicatorsByStation(int stationId){
		return sdiDao.getDeiviceIndicatorsByStation(stationId);
	}
	/*
	 * 根据模糊查询条件,查询出该用户角色下的所有站点列表
	 */
	public List<StationModel> getStation4DeviceIndicator(int userid,String code){
		return sdiDao.getStation4DeviceIndicator(userid, code);
	}
	public List<UiColumn> getCols4Stations(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col0 = new UiColumn("id", "id", false, "*");
		UiColumn col1 = new UiColumn("站点名称", "title", true, "*");
		UiColumn col2 = new UiColumn("所属单位", "companyName", true, "*");
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		return cols;
	}
	/*
	 * 保存站点设备参数
	 */
	public Result saveStationDeviceIndicator(StationDeviceIndicator model){
		return sdiDao.saveStationDeviceIndicator(model);
	}
}
