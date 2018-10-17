package com.sdocean.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.device.dao.DeviceDao;
import com.sdocean.device.model.DeviceModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class CommonService {
	
	@Autowired
	private DeviceDao deviceDao;
	
	
	/*
	 * 将 deviceid#indicatorid格式转换为
	 * list<deviceModel> 格式
	 */
	public List<DeviceModel> indicatoridsToDevices(String indicatorIds){
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
		return list;
	}
	
}
