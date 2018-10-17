package com.sdocean.main.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.main.dao.StationDeviceMainDao;
import com.sdocean.main.model.AiotMainConfigModel;
import com.sdocean.main.model.StationDeviceMainModel;
import com.sdocean.menu.dao.SysMenuDao;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.page.model.UiColumn;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class StationDeviceMainService {
	
	@Autowired
	StationDeviceMainDao stationDeviceMainDao;
	/*
	 * 为查询登录日志提供表头信息
	 */
	public List<UiColumn> getCols4MainList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点", "stationName", true, "*");
		UiColumn col4 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col5 = new UiColumn("设备", "deviceName", true, "*");
		UiColumn col6 = new UiColumn("amconfigId", "amconfigId", false, "*");
		UiColumn col7 = new UiColumn("例行维护", "mainConfigName", true, "*");
		UiColumn col8 = new UiColumn("维护周期", "mainNum", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		return cols;
	}
	/*
	 * 查询条件范围内的站点设备维护配置列表
	 */
	public List<StationDeviceMainModel> getStationDeviceMainList(StationDeviceMainModel model,List<StationModel> stations){
		return stationDeviceMainDao.getStationDeviceMainList(model, stations);
	}
	/*
	 * 保存新增的站点设备维护配置
	 */
	public Result saveStationDeviceMain(StationDeviceMainModel model){
		return stationDeviceMainDao.saveStationDeviceMain(model);
	}
	/*
	 * 保存修改的站点设备维护配置
	 */
	public Result saveChangeStationDeviceMain(StationDeviceMainModel model){
		return stationDeviceMainDao.saveChangeStationDeviceMain(model);
	}
	/*
	 * 删除选中的站点设备维护配置
	 */
	public Result deleStationDeviceMain(StationDeviceMainModel model){
		return stationDeviceMainDao.deleStationDeviceMain(model);
	}
	/*
	 * 根据站点和设备获得有效的例行维护种类的列表
	 */
	public List<AiotMainConfigModel> getAiotMainConfigListByStationDevice(StationDeviceMainModel model){
		return stationDeviceMainDao.getAiotMainConfigListByStationDevice(model);
	}
}
