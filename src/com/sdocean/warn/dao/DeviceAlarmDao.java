package com.sdocean.warn.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.metadata.model.MetadataModel;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationDeviceComm;
import com.sdocean.station.model.StationDeviceModel;
import com.sdocean.station.model.StationInfo;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.warn.model.DeviceAlarmConfig;
import com.sdocean.warn.model.DeviceAlarmModel;

@Component
public class DeviceAlarmDao extends OracleEngine {
	
	/*
	 * 读取设备报警码表
	 */
	public List<DeviceAlarmModel> getDeviceAlarmList(DeviceAlarmModel model,List<StationModel> stations){
		List<DeviceAlarmModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,c.title as stationname,a.deviceid,");
		sql.append(" d.name as devicename,a.configid,b.title as configname,");
		sql.append(" a.alarmdata,a.begintime,a.endtime");
		sql.append(" from aiot_device_alarm a,aiot_device_alarm_config b,");
		sql.append(" aiot_watch_point c,device_catalog d");
		sql.append(" where a.configid = b.id");
		sql.append(" and a.stationid = c.id and c.isactive = 1");
		sql.append(" and a.deviceid = d.id");
		//添加查询条件
		if(model!=null&&model.getStationId()>0) {
			sql.append(" and a.stationid = ").append(model.getStationId());
		}else {
			sql.append(" and a.stationid in (0");
			for(StationModel station:stations) {
				sql.append(",").append(station.getId());
			}
			sql.append(")");
		}
		//添加排序
		sql.append(" order by c.ordercode,d.ordercode");
		list = this.queryObjectList(sql.toString(), DeviceAlarmModel.class);
		return list;
	}
	
	/*
	 * 添加设备报警码表
	 */
	public Result saveNewDeviceAlarm(DeviceAlarmModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		
		//初始化开始结束时间
		if(model!=null) {
			if(model.getBeginTime()==null||model.getBeginTime().length()<1) {
				model.setBeginTime("2000-01-01 01:01:01");
			}
			if(model.getEndTime()==null||model.getEndTime().length()<1) {
				model.setEndTime("2030-01-01 01:01:01");
			}
		}
		//判断时间段与数据库现有的记录时间是否冲突
		StringBuffer csql = new StringBuffer("");
		csql.append(" select count(1) from aiot_device_alarm");
		csql.append(" where stationid =").append(model.getStationId());
		csql.append(" and deviceid =").append(model.getDeviceId());
		csql.append(" and configid =").append(model.getConfigId());
		csql.append(" and begintime <='").append(model.getEndTime()).append("'");
		csql.append(" and endtime >='").append(model.getBeginTime()).append("'");
		
		int cres = 0;
		try {
			cres = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("检查时间规则失败");
			return result;
		}
		if(cres>0){
			result.setResult(Result.FAILED);
			result.setMessage("时间设置不正确");
			return result;
		}
		
		//添加数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_device_alarm(stationid,deviceid,configid,alarmdata,begintime,endtime)");
		sql.append(" values(?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getStationId(),model.getDeviceId(),model.getConfigId(),
				model.getAlarmData(),model.getBeginTime(),model.getEndTime()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("新增失败");
			return result;
		}
		return result;
	}
	/*
	 * 修改站点设备配置
	 */
	public Result saveChangeDeviceAlaram(DeviceAlarmModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		
		//初始化开始结束时间
		if(model!=null) {
			if(model.getBeginTime()==null||model.getBeginTime().length()<1) {
				model.setBeginTime("2000-01-01 01:01:01");
			}
			if(model.getEndTime()==null||model.getEndTime().length()<1) {
				model.setEndTime("2030-01-01 01:01:01");
			}
		}
		//判断时间段与数据库现有的记录时间是否冲突
		StringBuffer csql = new StringBuffer("");
		csql.append(" select count(1) from aiot_device_alarm");
		csql.append(" where stationid =").append(model.getStationId());
		csql.append(" and deviceid =").append(model.getDeviceId());
		csql.append(" and configid =").append(model.getConfigId());
		csql.append(" and begintime <='").append(model.getEndTime()).append("'");
		csql.append(" and endtime >='").append(model.getBeginTime()).append("'");
		csql.append(" and id <> ").append(model.getId());		
		int cres = 0;
		try {
			cres = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("检查时间规则失败");
			return result;
		}
		if(cres>0){
			result.setResult(Result.FAILED);
			result.setMessage("时间设置不正确");
			return result;
		}
		//添加数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_device_alarm set stationid=?,deviceid=?,configid=?,alarmdata=?,begintime=?,endtime=? where id=?");
		Object[] params = new Object[]{
				model.getStationId(),model.getDeviceId(),model.getConfigId(),
				model.getAlarmData(),model.getBeginTime(),model.getEndTime(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("修改失败");
			return result;
		}
		return result;
	}
	/*
	 * 删除设备报警码
	 */
	public Result deleteDeviceAlarm(DeviceAlarmModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from aiot_device_alarm where id = ").append(model.getId());
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(Result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	
	/*
	 * 获得报警类型列表
	 */
	public List<DeviceAlarmConfig> getDeviceAlarmConfigList(DeviceAlarmConfig model){
		List<DeviceAlarmConfig> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select id,code,title,ordercode,remark");
		sql.append(" from aiot_device_alarm_config");
		sql.append(" where 1=1");
		//添加查询条件
		if(model!=null&&model.getId()>0) {
			sql.append(" and id = ").append(model.getId());
		}
		sql.append(" order by ordercode");
		list = this.queryObjectList(sql.toString(), DeviceAlarmConfig.class);
		return list;
	}
	
	/*
	 * 根据站点设备获得该设备的报警类型列表
	 */
	public List<DeviceAlarmModel> getDeviceAlarmListByStationDevice(DeviceAlarmModel model){
		List<DeviceAlarmModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,c.title as stationname,a.deviceid,");
		sql.append(" d.name as devicename,a.configid,b.title as configname,");
		sql.append(" a.alarmdata,a.begintime,a.endtime");
		sql.append(" from aiot_device_alarm a,aiot_device_alarm_config b,");
		sql.append(" aiot_watch_point c,device_catalog d");
		sql.append(" where a.configid = b.id");
		sql.append(" and a.stationid = c.id and c.isactive = 1");
		sql.append(" and a.deviceid = d.id");
		//添加查询条件
		if(model!=null&&model.getStationId()>0) {
			sql.append(" and a.stationid = ").append(model.getStationId());
		}
		if(model!=null&&model.getDeviceId()>0) {
			sql.append(" and a.deviceid = ").append(model.getDeviceId());
		}
		if(model!=null&&model.getBeginTime()!=null&&model.getBeginTime().length()>0) {
			sql.append(" and a.endTime >= '").append(model.getBeginTime()).append("'");
		}
		if(model!=null&&model.getEndTime()!=null&&model.getEndTime().length()>0) {
			sql.append(" and a.begintime <= '").append(model.getEndTime()).append("'");
		}
		//添加排序
		sql.append(" order by c.ordercode,d.ordercode");
		list = this.queryObjectList(sql.toString(), DeviceAlarmModel.class);
		return list;
	}
	
	/*
	 * 设备超量程后,读取量程数值
	 */
	public List<DeviceAlarmModel> getDeviceAlarmDataListByStationDeviceIndicator(
			int stationId,int deviceId,String indicatorCode,String beginTime,String endTime){
		List<DeviceAlarmModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.stationid,a.deviceid,a.configid,a.alarmdata,");
		sql.append(" a.begintime,a.endtime,b.maxdata");
		sql.append(" from aiot_device_alarm a,aiot_range_data b");
		sql.append(" where a.stationid = b.stationid");
		sql.append(" and a.deviceid = b.deviceid");
		sql.append(" and a.configid = 1");
		sql.append(" and a.stationid = ").append(stationId);
		sql.append(" and a.deviceid = ").append(deviceId);
		sql.append(" and b.indicatorcode='").append(indicatorCode).append("'");
		sql.append(" and a.endtime >= '").append(beginTime).append("'");
		sql.append(" and a.begintime <= '").append(endTime).append("'");
		list = this.queryObjectList(sql.toString(), DeviceAlarmModel.class);
		return list;
	}
	
	/*
	 * 根据当前的元数据,判断该数据是否为超量程数据,并得到他的最大值
	 */
	public DeviceAlarmModel getDeviceAlarmModelByMetaData(MetadataModel data) {
		DeviceAlarmModel dam = new DeviceAlarmModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.stationid,a.deviceid,a.configid,a.alarmdata,");
		sql.append(" a.begintime,a.endtime,b.maxdata");
		sql.append(" from aiot_device_alarm a,aiot_range_data b");
		sql.append(" where a.stationid = b.stationid");
		sql.append(" and a.deviceid = b.deviceid");
		sql.append(" and a.configid = 1");
		sql.append(" and a.stationid = ").append(data.getWpId());
		sql.append(" and a.deviceid = ").append(data.getDeviceId());
		sql.append(" and b.indicatorcode='").append(data.getIndicator_code()).append("'");
		sql.append(" and a.alarmdata=").append(data.getData());
		sql.append(" and a.begintime<='").append(data.getCollect_time()).append("'");
		sql.append(" and a.endtime>='").append(data.getCollect_time()).append("'");
		sql.append(" limit 1");
		dam = this.queryObject(sql.toString(), DeviceAlarmModel.class);
		return dam;
	}
}
