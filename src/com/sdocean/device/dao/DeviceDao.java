package com.sdocean.device.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Component
public class DeviceDao extends OracleEngine{
	
	/*
	 * 查询符合条件的设备列表
	 */
	public List<DeviceModel> getDevices(DeviceModel model){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name,a.code,a.deviceModel,a.brief,a.detail,a.ordercode,");
		sql.append(" group_concat(m.title SEPARATOR '|') as indicatorNames,");
		sql.append(" group_concat(m.indicatorid) as indicatorIds");
		sql.append(" from device_catalog a left join (select b.catalogId,b.indicatorid,c.title");
		sql.append(" from device_catalog_indicator b,dm_indicator c");
		sql.append(" where b.indicatorid = c.id and c.isactive = 1) m");
		sql.append(" on a.id = m.catalogid");
		sql.append(" where 1=1");
		//添加查询条件
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and ( a.name like '%").append(model.getCode()).append("%' or");
			sql.append(" a.code like '%").append(model.getCode()).append("%') ");
		}
		sql.append(" group by a.id");
		//添加排序
		sql.append(" order by ordercode ");
		list = this.queryObjectList(sql.toString(), DeviceModel.class);
		return list;
	}
	/*
	 * 根据设备ID,查询设备信息
	 */
	public DeviceModel getDeviceByDeviceId(int deviceId){
		DeviceModel device = new DeviceModel();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name,a.code,a.deviceModel,a.brief,a.detail,a.ordercode,");
		sql.append(" group_concat(m.title SEPARATOR '|') as indicatorNames,");
		sql.append(" group_concat(m.indicatorid) as indicatorIds");
		sql.append(" from device_catalog a left join (select b.catalogId,b.indicatorid,c.title");
		sql.append(" from device_catalog_indicator b,dm_indicator c");
		sql.append(" where b.indicatorid = c.id and c.isactive = 1) m");
		sql.append(" on a.id = m.catalogid");
		sql.append(" where a.id =").append(deviceId);
		sql.append(" limit 1");
		device = this.queryObject(sql.toString(), DeviceModel.class);
		return device;
	}
	
	/*
	 * 保存修改的设备信息
	 */
	public Result saveDeviceChange(DeviceModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//验证code 是否唯一
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from device_catalog where code ='").append(model.getCode()).append("' and id <>").append(model.getId());
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("验证唯一性时失败");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("CODE重复,违反唯一性原则");
			return result;
		}
		//开始保存数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" update device_catalog set name=?,code=?,devicemodel=?,brief=?,detail=?,ordercode=? where id =?");
		Object[] params = new Object[]{
				model.getName(),model.getCode(),model.getDeviceModel(),
				model.getBrief(),model.getDetail(),model.getOrderCode(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		
		this.saveDeviceIndicator(model);
		return result;
	}
	
	 /*
	  * 保存新增的设备
	  */
	public Result saveNewDevice(DeviceModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//验证code 是否唯一
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from device_catalog where code ='").append(model.getCode()).append("' ");
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("验证唯一性时失败");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("CODE重复,违反唯一性原则");
			return result;
		}
		//开始保存数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into device_catalog(name,code,devicemodel,brief,detail,ordercode) values(?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getName(),model.getCode(),model.getDeviceModel(),
				model.getBrief(),model.getDetail(),model.getOrderCode()
		};
		int res = 0;
		try {
			
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
			return result;
		}
		
		//获得保存的ID值\
		String last = "SELECT LAST_INSERT_ID()";
		int lastId = this.queryForInt(last, null);
		model.setId(lastId);
		this.saveDeviceIndicator(model);
		return result;
	}
	
	public void saveDeviceIndicator(DeviceModel model){
		//删除设备的参数
		StringBuffer deleSql = new StringBuffer("");
		deleSql.append(" delete from device_catalog_indicator where  catalogid = ").append(model.getId());
		this.update(deleSql.toString(), null);
		String ids = model.getIndicatorIds();
		String[] idStrings = ids.split(",");
		StringBuffer val = new StringBuffer("(0,0)");
		for(String id:idStrings){
			val.append(",(").append(model.getId()).append(",").append(id).append(")");
		}
		
		StringBuffer insertSql = new StringBuffer("");
		insertSql.append(" insert into device_catalog_indicator(catalogid,indicatorid) values").append(val);
		insertSql.append(" ON DUPLICATE KEY UPDATE indicatorid = indicatorid ");
		this.update(insertSql.toString(), null);
	}
	
	/*
	 * 获得当前站点所关联的设备列表
	 */
	public List<SelectTree> getDeviceListByStation(StationModel model){
		List<SelectTree> list = new ArrayList<SelectTree>();
		//添加设备列表的首层
		SelectTree first = new SelectTree();
		first.setId("0");
		first.setName("设备");
		first.setIsExpanded(true);
		first.setIsActive(true);
		
		//获得所有有效的设备的列表
		List<SelectTree> children = new ArrayList<SelectTree>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name, case when b.aiot_watch_point_id is null then 'false' else 'true' end as selected");
		sql.append(" from device_catalog a");
		sql.append(" left join map_awp_device_catalog b on a.id = b.device_catalog_id");
		sql.append(" and b.aiot_watch_point_id = ").append(model.getId());
		children = this.queryObjectList(sql.toString(), SelectTree.class);
		//将设备列表添加的首层的child中
		first.setChildren(children);
		//将首层添加到结果集中
		list.add(first);
		return list;
	}
	
	/*
	 * 获得当前站点下的设备的列表
	 */
	public List<DeviceModel> getDevices4Station(StationModel model){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct a.id,a.name,a.code,a.deviceModel,a.brief,a.detail,a.ordercode");
		sql.append(" from device_catalog a,map_awp_device_catalog b");
		sql.append(" where a.id = b.device_catalog_id");
		//添加查询条件
		if(model!=null&&model.getId()>0){
			sql.append(" and b.aiot_watch_point_id =").append(model.getId());
		}
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), DeviceModel.class);
		return list;
	}
	/*
	 * 获得当前站点下有展示权限的设备的列表
	 */
	public List<DeviceModel> getDevices4Station4Show(StationModel model){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name,a.code,a.deviceModel,a.brief,a.detail,a.ordercode");
		sql.append(" from view_show_station_device a");
		sql.append(" where a.stationid =").append(model.getId());
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), DeviceModel.class);
		return list;
	}
	/*
	 *根据deviceid以及indicators 
	 */
	public DeviceModel getDeviceByid(String deviceId,String indicatorids){
		DeviceModel model = new DeviceModel();
		//查询出设备
		StringBuffer devicesql = new StringBuffer("");
		devicesql.append(" select a.id,a.name,a.code,a.deviceModel,a.brief,a.detail,a.ordercode");
		devicesql.append(" from device_catalog a where a.id = ").append(deviceId);
		model = this.queryObject(devicesql.toString(), DeviceModel.class);
		//查询出参数列表
		StringBuffer indicatorSql = new StringBuffer("");
		indicatorSql.append(" select a.id,a.code,a.title,a.groupId,a.unitId,b.logo as unitName,a.description,a.isactive");
		indicatorSql.append(" from dm_indicator a,g_unit b where a.unitid = b.id and  a.id in (").append(indicatorids).append(")");
		List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();
		indicators = this.queryObjectList(indicatorSql.toString(), IndicatorModel.class);
		if(indicators!=null&&indicators.size()>0){
			model.setIndicators(indicators);
		}
		return model;
	}
	
	/*
	 * 读取站点内设备读取数据的时间间隔
	 */
	public DeviceModel getInterval4device(int stationid,DeviceModel device){
		StringBuffer sql = new StringBuffer("");
		sql.append("select max(betweentime) from aiot_station_device_comm where stationid in (0, ").append(stationid).append(") and deviceid in (0").append(device.getId()).append(")");
		Integer inter = this.queryForInt(sql.toString(), null);
		if(inter==null){
			inter = 0;
		}
		device.setInterval(inter);
		return device;
	}
	
	/*
	 * 获得当前站点下有展示权限的系统设备的列表
	 */
	public List<DeviceModel> getDevices4Station4System(StationModel model){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name,a.code,a.deviceModel,a.brief,a.detail,a.ordercode");
		sql.append(" from view_system_station_device a");
		sql.append(" where a.stationid =").append(model.getId());
		sql.append(" order by a.ordercode");
		list = this.queryObjectList(sql.toString(), DeviceModel.class);
		return list;
	}
}
