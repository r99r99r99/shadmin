package com.sdocean.station.dao;

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
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationDeviceComm;
import com.sdocean.station.model.StationDeviceModel;
import com.sdocean.station.model.StationInfo;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;

@Component
public class StationDeviceDao extends OracleEngine {
	
	/*
	 * 读取站点设备配置列表
	 */
	public List<StationDeviceModel> getStationDeviceList(StationDeviceModel model,List<StationModel> stations){
		List<StationDeviceModel> list = new ArrayList<StationDeviceModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,b.id as stationId,b.title as stationName,c.id as deviceId,c.name as deviceName,");
		sql.append(" a.pointNum,a.createtime,a.ordercode");
		sql.append(" from map_awp_device_catalog a,aiot_watch_point b,device_catalog c");
		sql.append(" where a.aiot_watch_point_id = b.id and a.device_catalog_id = c.id");
		sql.append(" and b.isactive = 1");
		//添加查询条件
		if(model!=null&&model.getStationId()>0){
			sql.append(" and b.id = ").append(model.getStationId());
		}else{
			StringBuffer ssql = new StringBuffer("");
			ssql.append("(0");
			for(StationModel station:stations){
				ssql.append(",").append(station.getId());
			}
			ssql.append(")");
			sql.append(" and b.id in ").append(ssql);
		}
		//增加排序
		sql.append(" order by b.ordercode,a.ordercode");
		list = this.queryObjectList(sql.toString(), StationDeviceModel.class);
		return list;
	}
	
	/*
	 * 添加站点设备配置
	 */
	public Result saveNewStationDevice(StationDeviceModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		
		
		
		//判断是否违法唯一性原则
		StringBuffer csql = new StringBuffer("");
		csql.append(" select count(1) from map_awp_device_catalog where aiot_watch_point_id =").append(model.getStationId());
		csql.append(" and device_catalog_id = ").append(model.getDeviceId());
		int cres = 0;
		try {
			cres = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("唯一性检查时失败");
			return result;
		}
		if(cres>0){
			result.setResult(Result.FAILED);
			result.setMessage("违法唯一性原则");
			return result;
		}
		//添加数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into map_awp_device_catalog(aiot_watch_point_id,device_catalog_id,pointnum,ordercode,createtime)");
		sql.append(" values(?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getStationId(),model.getDeviceId(),model.getPointNum(),model.getOrderCode(),model.getCreateTime()
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
	public Result saveChangeStationDevice(StationDeviceModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		
		//判断是否违法唯一性原则
		StringBuffer csql = new StringBuffer("");
		csql.append(" select count(1) from map_awp_device_catalog where aiot_watch_point_id =").append(model.getStationId());
		csql.append(" and device_catalog_id = ").append(model.getDeviceId());
		csql.append(" and id <> ").append(model.getId());
		int cres = 0;
		try {
			cres = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("唯一性检查时失败");
			return result;
		}
		if(cres>0){
			result.setResult(Result.FAILED);
			result.setMessage("违法唯一性原则");
			return result;
		}
		//添加数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" update map_awp_device_catalog set aiot_watch_point_id=?,device_catalog_id=?,pointnum=?,ordercode=?,createtime=? where id=?");
		Object[] params = new Object[]{
				model.getStationId(),model.getDeviceId(),model.getPointNum(),model.getOrderCode(),model.getCreateTime(),model.getId()
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
	 * 删除站点设备配置
	 */
	public Result deleteStationDevice(StationDeviceModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from map_awp_device_catalog where id = ").append(model.getId());
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
	 * 根据站点Id以及设备Id获得站点设备配置
	 */
	public StationDeviceModel getStationDeviceByWidDid(StationDeviceModel model){
		StationDeviceModel stationDevice = new StationDeviceModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select id,aiot_watch_point_id as stationid,device_catalog_id as deviceId,pointnum,ordercode,createtime");
		sql.append(" from map_awp_device_catalog where aiot_watch_point_id =").append(model.getStationId()).append(" and device_catalog_id =").append(model.getDeviceId());
		sql.append(" limit 1");
		stationDevice = this.queryObject(sql.toString(), StationDeviceModel.class);
		return stationDevice;
	}
}
