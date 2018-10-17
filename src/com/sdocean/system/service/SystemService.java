package com.sdocean.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.page.model.UiColumn;
import com.sdocean.sms.dao.SmsDao;
import com.sdocean.sms.model.SmsLog;
import com.sdocean.sms.model.SmsMouldModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.station.dao.StationDao;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.system.dao.SystemDao;
import com.sdocean.system.model.SystemWarnModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class SystemService {

	@Resource
	private StationDao stationDao;
	@Resource
	private SystemDao systemDao;
	
	private static double EARTH_RADIUS = 6378.137;   
	
	//得到当前用户的GPS告警信息
	public List<SystemWarnModel> getGpsWarn(SysUser user){
		
		//初始化返回结果
		List<SystemWarnModel> list = new ArrayList<>();
		//获得用户GPS站点权限的站点列表
		List<StationModel> stations = new ArrayList<>();
		stations = stationDao.getGpsStations4User(user);
		for(StationModel station:stations){
			SystemWarnModel warn = new SystemWarnModel();
			//获得该用户上一次的预警的时间,如果没有,则给出默认值
			warn = systemDao.getLastGpsWarn(user, station);
			if(warn==null){
				warn = new SystemWarnModel();
				warn.setLastTime("2016-09-01");
			}
			//从数据库中读取上一次预警时间后的站点的浮标信息
			SystemWarnModel nowData = systemDao.getNowGps(station, warn.getLastTime());
			//将这次读取的数据时间更新到配置表中
			if(nowData!=null){
				systemDao.saveChangeLastGps(user, station, nowData.getLastTime());
				//获得当前经纬度与设置间的距离
				if(nowData.getNowLatitude()!=null&&nowData.getNowLatitude()>0
						&&nowData.getNowLongitude()!=null&&nowData.getNowLongitude()>0){
					Double distance = this.getDistance(nowData.getNowLatitude(), nowData.getNowLongitude(), station.getLatitude(), station.getLongitude());
					if(distance>station.getWarndistance()){//如果距离超过告警距离,生成告警信息
						
					}
				}
			}
			
		}
		return list;
	}
	
	private static double rad(double d) {    
        return d * Math.PI / 180.0;    
    } 
	
	public static double getDistance(double lat1, double lng1, double lat2,    
            double lng2) {    
		double radLat1 = rad(lat1);    
		double radLat2 = rad(lat2);    
		double a = radLat1 - radLat2;    
		double b = rad(lng1) - rad(lng2);    
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)    
		+ Math.cos(radLat1) * Math.cos(radLat2)    
		* Math.pow(Math.sin(b / 2), 2)));    
		s = s * EARTH_RADIUS;    
		s = Math.round(s * 10000d) / 10000d;    
		s = s*1000;
		
		return s;    
	}    
}
