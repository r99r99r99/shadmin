package com.sdocean.system.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.DoubleModel;
import com.sdocean.common.model.Result;
import com.sdocean.firstpage.model.SystemModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.sms.model.SmsLog;
import com.sdocean.sms.model.SmsMouldModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.system.model.SystemWarnModel;
import com.sdocean.users.model.SysUser;

import freemarker.template.utility.StringUtil;

@Component
public class SystemDao extends OracleEngine{
	
	//获得上一次
	public SystemWarnModel getLastGpsWarn(SysUser user,StationModel station){
		SystemWarnModel last = new SystemWarnModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select userid,stationid,lasttime");
		sql.append(" from sys_user_gps_station");
		sql.append(" where userid = ").append(user.getId());
		sql.append(" and stationid = ").append(station.getId());
		last = this.queryObject(sql.toString(), SystemWarnModel.class);
		return last;
	}
	
	
	//从数据库中获得最近一次的经纬度信息
	public SystemWarnModel getNowGps(StationModel station,String lastTime){
		SystemWarnModel result = new SystemWarnModel();
		//首先获得维度信息
		StringBuffer sql = new StringBuffer("");
		sql.append(" select collect_time as lasttime, data as nowLatitude");
		sql.append(" from aiot_metadata_system");
		sql.append(" where wpid = ").append(station.getId());
		sql.append(" and indicator_code = 'latitude'");
		sql.append(" and collect_time > '").append(lastTime).append("'");
		sql.append(" order by collect_time desc limit 1");
		result = this.queryObject(sql.toString(), SystemWarnModel.class);
		if(result!=null){//获得经度信息
			SystemWarnModel longitude = new SystemWarnModel();
			StringBuffer csql = new StringBuffer("");
			csql.append(" select collect_time as lasttime, data as nowLongitude");
			csql.append(" from aiot_metadata_system");
			csql.append(" where wpid = ").append(station.getId());
			csql.append(" and indicator_code = 'ongitude'");
			csql.append(" and collect_time > '").append(lastTime).append("'");
			csql.append(" order by collect_time desc limit 1");
			longitude = this.queryObject(csql.toString(), SystemWarnModel.class);
			if(longitude!=null){
				result.setNowLongitude(longitude.getNowLongitude());
			}
		}
		return result;
	}
	
	public void saveChangeLastGps(SysUser user,StationModel station,String lastTime){
		StringBuffer sql =  new StringBuffer("");
		sql.append(" insert into sys_user_gps_station(userid,stationid,lasttime)");
		sql.append(" values(?,?,?)");
		sql.append(" on duplicate key update lasttime =values(lasttime)");
		Object[] param = new Object[]{
				user.getId(),station.getId(),lastTime
		};
		this.update(sql.toString(), param);
	}
	
	//读取站点状态表中,某一参数的最新的数值
	public DoubleModel getDataByStationIdIndicatorCode(int stationId,String indicatorCode){
		DoubleModel result = null;
		StringBuffer sql = new StringBuffer("");
		sql.append(" select data as result from aiot_metadata_system");
		sql.append(" where wpid = ").append(stationId).append(" and indicator_code = '").append(indicatorCode).append("'");
		sql.append(" order by collect_time desc limit 1");
		result = this.queryObject(sql.toString(), DoubleModel.class);
		return result;
	}
}
