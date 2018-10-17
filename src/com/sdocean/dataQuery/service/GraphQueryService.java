package com.sdocean.dataQuery.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Echarts;
import com.sdocean.common.model.HchartsServieModel;
import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.dataQuery.dao.DataQueryDao;
import com.sdocean.dataQuery.dao.GraphQueryDao;
import com.sdocean.dataQuery.dao.SynthQueryDao;
import com.sdocean.dataQuery.model.ComparisonModel;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.GraphModel;
import com.sdocean.device.dao.DeviceDao;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.dao.PublicDao;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.dao.SysLoginLogDao;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.page.model.NgColumn;
import com.sdocean.position.dao.SysPositionDao;
import com.sdocean.position.model.SysPosition;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class GraphQueryService {
	
	@Autowired
	private DeviceDao deviceDao;
	@Autowired
	private GraphQueryDao graphDao;
	
	/*
	 * 初始化参数列表
	 */
	public void modelInfo(GraphModel model){
		String indicatorIds = model.getIndicatorIds();
		String[] ids = indicatorIds.split(",");
		Map<String, String> deids = new HashMap<String, String>();
		for(String id:ids){
			String indicatorid = id.substring(0, id.indexOf("#"));
			String deviceid = id.substring(id.indexOf("#")+1,id.length());
			if(deids.containsKey(deviceid)){
				String indi = deids.get(deviceid)+","+indicatorid;
				deids.remove(deviceid);
				deids.put(deviceid, indi);
			}else{
				deids.put(deviceid, indicatorid);
			}
		}
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		for(String deviceId:deids.keySet()){
			String indicatorid = deids.get(deviceId);
			DeviceModel device = deviceDao.getDeviceByid(deviceId, indicatorid);
			if(device!=null&&device.getIndicators()!=null&&device.getIndicators().size()>0){
				list.add(device);
			}
		}
		model.setDevices(list);
	}
	
	/*
	 * 水质评价 综合查询
	 */
	public List<HchartsServieModel> getEcharts4Graph(StationModel station,GraphModel model){
		return graphDao.getEcharts4Graph(station, model);
	}
	/*
	 * 为综合对比查询查询提供数据
	 */
	public ComparisonModel getComparisonResult(ComparisonModel model){
		return graphDao.getComparisonResult(model);
	}
}
