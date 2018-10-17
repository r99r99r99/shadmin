package com.sdocean.warn.service;

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
import com.sdocean.warn.dao.DeviceAlarmDao;
import com.sdocean.warn.model.DeviceAlarmConfig;
import com.sdocean.warn.model.DeviceAlarmModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class DeviceAlarmService {

	@Resource
	private DeviceAlarmDao deviceAlarmDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4DeviceAlarmList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col0 = new UiColumn("id", "id", false, "*");
		UiColumn col1 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col2 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col3 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col6 = new UiColumn("设备名称", "deviceName", true, "*");
		UiColumn col7 = new UiColumn("configId", "configId", false, "*");
		UiColumn col8 = new UiColumn("报警类型", "configName", true, "*");
		UiColumn col9 = new UiColumn("报警码", "alarmData", true, "*");
		UiColumn col10 = new UiColumn("开始时间", "beginTime", true, "*");
		UiColumn col11 = new UiColumn("结束时间", "endTime", true, "*");
		
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col11);
		return cols;
	}
	/*
	 * 读取设备报警码表
	 */
	public List<DeviceAlarmModel> getDeviceAlarmList(DeviceAlarmModel model,List<StationModel> stations){
		return deviceAlarmDao.getDeviceAlarmList(model, stations);
	}

	/*
	 * 添加设备报警码表
	 */
	public Result saveNewDeviceAlarm(DeviceAlarmModel model){
		return deviceAlarmDao.saveNewDeviceAlarm(model);
	}
	/*
	 * 修改站点设备配置
	 */
	public Result saveChangeDeviceAlaram(DeviceAlarmModel model){
		return deviceAlarmDao.saveChangeDeviceAlaram(model);
	}
	/*
	 * 删除设备报警码
	 */
	public Result deleteDeviceAlarm(DeviceAlarmModel model){
		return deviceAlarmDao.deleteDeviceAlarm(model);
	}
	/*
	 * 获得报警类型列表
	 */
	public List<DeviceAlarmConfig> getDeviceAlarmConfigList(DeviceAlarmConfig model){
		return deviceAlarmDao.getDeviceAlarmConfigList(model);
	}
}
