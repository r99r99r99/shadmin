package com.sdocean.station.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationDeviceIndicator;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;

@Component
public class StationDeviceIndicatorDao extends OracleEngine {
	
	/*
	 * 根据站点,以ztree的形式展示有显示权限的设备以及关联的参数
	 */
	public List<ZTreeModel> getDeiviceIndicatorsByStation(int stationId){
		List<ZTreeModel> alllist = new ArrayList<ZTreeModel>();
		//添加首行记录
		ZTreeModel first = new ZTreeModel();
		first.setId("0");
		first.setName("设备/参数");
		first.setOpen(true);
		alllist.add(first);
		for(ZTreeModel all:alllist){
			List<ZTreeModel> list = new ArrayList<ZTreeModel>();
			//获得当前站点下的设备列表
			StringBuffer deviceSql = new StringBuffer("");
			deviceSql.append(" select a.id,0 as pid,a.name as name,");
			deviceSql.append(" case when m.deviceid is null then 'false' else 'true' end as checked");
			deviceSql.append(" from device_catalog a");
			deviceSql.append(" left join (select distinct deviceid from aiot_station_device_indicator ");
			deviceSql.append(" where stationid =").append(stationId).append(" ) m ");
			deviceSql.append(" on a.id = m.deviceid,");
			deviceSql.append(" map_awp_device_catalog b");
			deviceSql.append(" where b.aiot_watch_point_id = ").append(stationId);
			deviceSql.append(" and a.id = b.device_catalog_id");
			deviceSql.append(" order by a.ordercode");
			
			list = this.queryObjectList(deviceSql.toString(), ZTreeModel.class);
			//遍历设备列表,获得设备下的所属的参数列表
			for(ZTreeModel device:list){
				StringBuffer indicatorSql = new StringBuffer("");
				indicatorSql.append(" select concat(").append(device.getId()).append(",'#',a.id) as id,").append(device.getId()).append(" as pid,a.title as name,");
				indicatorSql.append(" case when  m.indicatorid is null then 'false' else 'true' end as checked");
				indicatorSql.append(" from dm_indicator a left join (select distinct indicatorid from aiot_station_device_indicator ");
				indicatorSql.append(" where stationid =").append(stationId).append(" and deviceid =").append(device.getId()).append(" ) m");
				indicatorSql.append(" on a.id = m.indicatorid,");
				indicatorSql.append(" device_catalog_indicator b");
				indicatorSql.append(" where a.isactive = 1");
				indicatorSql.append(" and a.id = b.indicatorid");
				indicatorSql.append(" and b.catalogid = ").append(device.getId());
				List<ZTreeModel> children = new ArrayList<ZTreeModel>();
				children = this.queryObjectList(indicatorSql.toString(), ZTreeModel.class);
				device.setChildren(children);
			}
			all.setChildren(list);
		}
		
		return alllist;
	}
	
	/*
	 * 根据模糊查询条件,查询出该用户角色下的所有站点列表
	 */
	public List<StationModel> getStation4DeviceIndicator(int userid,String code){
		List<StationModel> stations = new ArrayList<StationModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct a.id,a.title,b.name as companyName");
		sql.append(" from view_role_user_station a, g_company b");
		sql.append(" where  a.companyid = b.code");
		//增加条件
		sql.append(" and a.userid = ").append(userid);
		//增加模糊查询条件
		if(code!=null&&code.length()>0){
			sql.append(" and (a.title like '%").append(code).append("%'");
			sql.append(" or b.name like '%").append(code).append("%' )");
		}
		//增加排序
		sql.append(" order by b.ordercode");
		stations = this.queryObjectList(sql.toString(), StationModel.class);
		return stations;
	}
	/*
	 * 保存站点设备参数
	 */
	public Result saveStationDeviceIndicator(StationDeviceIndicator model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//删除该站点的原有数据
		StringBuffer deleSql = new StringBuffer("");
		deleSql.append("delete from aiot_station_device_indicator where stationid =").append(model.getStationId());
		int deleRes = 0;
		try {
			deleRes = this.update(deleSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除原有数据时失败");
			return result;
		}
		//开始保存数据
		//定义总体sql
		StringBuffer sql = new StringBuffer("");
		sql.append("insert  into aiot_station_device_indicator(stationid,deviceid,indicatorid) values");
		StringBuffer valueSql = new StringBuffer("");
		valueSql.append("(0,0,0)");
		//获得参数集合
		String indicatorIds = model.getIndicatorIds();
		String[] codes = indicatorIds.split(",");
		for(int i=0;i<codes.length;i++){
			String code= codes[i];
			String deviceId = code.substring(0, code.indexOf("#"));
			String indicatorId = code.substring(code.indexOf("#")+1, code.length());
			valueSql.append(",(").append(model.getStationId()).append(",").append(deviceId).append(",").append(indicatorId).append(")");
		}
		sql.append(valueSql).append(" on duplicate key update indicatorid=values(indicatorid)");
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		//将保存的站点 设备参数信息同时保存到站点首页显示设置中
		StringBuffer  firstSql = new StringBuffer("");
		firstSql.append("insert  into aiot_firstpage_show(wpid,deviceid,indicatorid) values");
		firstSql.append(valueSql).append(" on duplicate key update indicatorid=values(indicatorid)");
		try {
			this.update(firstSql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("保存首页参数显示设置失败!");
			return result;
		}
		
		return result;
	}
}
