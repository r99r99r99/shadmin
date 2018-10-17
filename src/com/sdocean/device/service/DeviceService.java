package com.sdocean.device.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.device.dao.DeviceDao;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class DeviceService {
	
	@Autowired
	private DeviceDao deviceDao;
	/*
	 * 为公共代码管理提供表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", true, "*");
		UiColumn col2 = new UiColumn("设备名称", "name", true, "*");
		UiColumn col3 = new UiColumn("设备编码", "code", true, "*");
		UiColumn col4 = new UiColumn("设备模型", "deviceModel", true, "*");
		UiColumn col5 = new UiColumn("介绍", "brief", true, "*");
		UiColumn col6 = new UiColumn("明细", "detail", true, "*");
		UiColumn col19 = new UiColumn("orderCode", "orderCode", true, "*");
		UiColumn col11 = new UiColumn("监测参数", "indicatorNames", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col4);
		cols.add(col5);
		cols.add(col19);
		cols.add(col11);
		return cols;
	}
	

	/*
	 * 查询符合条件的设备列表
	 */
	public List<DeviceModel> getDevices(DeviceModel model){
		return deviceDao.getDevices(model);
	}
	
	/*
	 * 保存修改的设备信息
	 */
	public Result saveDeviceChange(DeviceModel model){
		return deviceDao.saveDeviceChange(model);
	}
	
	/*
	  * 保存新增的设备
	  */
	public Result saveNewDevice(DeviceModel model){
		return deviceDao.saveNewDevice(model);
	}
	
	/*
	 * 获得当前站点所关联的设备列表
	 */
	public List<SelectTree> getDeviceListByStation(StationModel model){
		return deviceDao.getDeviceListByStation(model);
	}
	
	/*
	 * 获得当前站点下的设备的列表
	 */
	public List<DeviceModel> getDevices4Station(StationModel model){
		return deviceDao.getDevices4Station(model);
	}
	/*
	 * 获得当前站点下有展示权限的设备的列表
	 */
	public List<DeviceModel> getDevices4Station4Show(StationModel model){
		return deviceDao.getDevices4Station4Show(model);
	}
	/*
	 * 获得当前站点下有展示权限的系统设备的列表
	 */
	public List<DeviceModel> getDevices4Station4System(StationModel model){
		return deviceDao.getDevices4Station4System(model);
	}
}
