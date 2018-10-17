package com.sdocean.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.dao.StationCommDao;
import com.sdocean.station.dao.StationDeviceDao;
import com.sdocean.station.model.StationDeviceComm;
import com.sdocean.station.model.StationDeviceModel;
import com.sdocean.station.model.StationInfo;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StationDeviceService {

	@Resource
	private StationDeviceDao stationDeviceDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4StationDeviceList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col0 = new UiColumn("id", "id", false, "*");
		UiColumn col1 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col2 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col3 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col6 = new UiColumn("设备名称", "deviceName", true, "*");
		UiColumn col7 = new UiColumn("小数点位数", "pointNum", true, "*");
		UiColumn col8 = new UiColumn("设备安装时间", "createTime", true, "*");
		UiColumn col9 = new UiColumn("排序编码", "orderCode", true, "*");
		
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		return cols;
	}

	/*
	 * 读取站点设备配置列表
	 */
	public List<StationDeviceModel> getStationDeviceList(StationDeviceModel model,List<StationModel> stations){
		return stationDeviceDao.getStationDeviceList(model,stations);
	}
	/*
	 * 添加站点设备配置
	 */
	public Result saveNewStationDevice(StationDeviceModel model){
		return stationDeviceDao.saveNewStationDevice(model);
	}
	/*
	 * 修改站点设备配置
	 */
	public Result saveChangeStationDevice(StationDeviceModel model){
		return stationDeviceDao.saveChangeStationDevice(model);
	}
	/*
	 * 删除站点设备配置
	 */
	public Result deleteStationDevice(StationDeviceModel model){
		return stationDeviceDao.deleteStationDevice(model);
	}
	/*
	 * 根据站点Id以及设备Id获得站点设备配置
	 */
	public StationDeviceModel getStationDeviceByWidDid(StationDeviceModel model){
		return stationDeviceDao.getStationDeviceByWidDid(model);
	}
}
