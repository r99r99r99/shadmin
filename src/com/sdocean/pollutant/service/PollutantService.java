package com.sdocean.pollutant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.pollutant.dao.PollutantDao;
import com.sdocean.pollutant.model.Pollutant4First;
import com.sdocean.pollutant.model.PollutantModel;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class PollutantService {
	@Autowired
	private PollutantDao pollutantDao;
	
	/*
	 * 首页展示入海污染量
	 */
	/*
	 * 为首页展示入海污染量提供数据
	 */
	public Pollutant4First getPollutants4FirstPage(StationModel station){
		Pollutant4First first = pollutantDao.getPollutants4FirstPage(station);
		return first;
	}
	
	/*
	 * 入海污染源查询提供表头
	 */
	public List<UiColumn> getColumns4Polluinfo(PollutantModel model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		cols = pollutantDao.getColumns4Polluinfo(model);
		return cols;
	}
	/*
	 * 为入海污染量查询提供结果集
	 */
	public List<Map<String, Object>> getRows4Polluinfo(PollutantModel model){
		return pollutantDao.getRows4Polluinfo(model);
	}
	/*
	 * 为入海污染源设置查询提供表头
	 */
	public List<UiColumn> getColumns4PolluSetinfo(PollutantModel model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col0 = new UiColumn("id", "id", false, "*");
		UiColumn col1 = new UiColumn("wpId", "wpId", false, "*");
		UiColumn col2 = new UiColumn("站点", "wpName", true, "*");
		UiColumn col3 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col6 = new UiColumn("设备", "deviceName", true, "*");
		UiColumn col7 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col8 = new UiColumn("参数", "indicatorName", true, "*");
		UiColumn col9 = new UiColumn("type", "type", false, "*");
		UiColumn col10 = new UiColumn("类型", "typeName", true, "*");
		
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		return cols;
	}
	
	/*
	 * 为入海污染源设置查询提供结果集
	 */
	public List<PollutantModel> getPolluSetList(PollutantModel model){
		return pollutantDao.getPolluSetList(model);
	}
	/*
	 * 添加入海污染源设置
	 */
	public Result saveNewPolluSet(PollutantModel model){
		return pollutantDao.saveNewPolluSet(model);
	}
	/*
	 * 修改入海污染源设置
	 */
	public Result savePolluSet(PollutantModel model){
		return pollutantDao.savePolluSet(model);
	}
	/*
	 * 删除入海污染源设置
	 */
	public Result delePolluSet(PollutantModel model){
		return pollutantDao.delePolluSet(model);
	}
	/*
	 * 为首页的入海污染物查询提供表头
	 */
	public List<UiColumn> getColumns4Polluinfo4First(){
		return pollutantDao.getColumns4Polluinfo4First();
	}
	/*
	 * 获得站点需要展示的入海污染量的信息
	 */
	public List<DeviceModel> getDevices4Pollu(StationModel station){
		return pollutantDao.getDevices4Pollu(station);
	}
}
