package com.sdocean.firstpage.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.firstpage.model.FirstPageShow;
import com.sdocean.firstpage.model.SystemModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.station.model.StationModel;

@Component
public class FirstPageDao extends OracleEngine{
	
	/*
	 * 为首页实时数据管理查询数据
	 */
	public List<FirstPageShow> getFirstPageShowList(FirstPageShow model){
		List<FirstPageShow> list = new ArrayList<FirstPageShow>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.wpid,b.title as wpName,a.indicatorid,d.title as indicatorname,");
		sql.append(" a.deviceid,c.name as deviceName");
		sql.append(" from aiot_firstpage_show a,aiot_watch_point b,device_catalog c,dm_indicator d");
		sql.append(" where a.wpid = b.id and b.isactive = 1");
		sql.append(" and a.deviceid = c.id and a.indicatorid = d.id and d.isactive = 1");
		//添加查询条件
		if(model!=null&&model.getWpId()>10000){
			sql.append(" and a.wpid = ").append(model.getWpId());
		}
		list = this.queryObjectList(sql.toString(), FirstPageShow.class);
		return list;
	}
	
	/*
	 *  保存修改首页实时数据管理
	 */
	public Result saveFirstPageShowChange(FirstPageShow model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//检查是否唯一原则
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_firstpage_show where wpid=? and deviceid=? and indicatorid=? and id <> ?");
		Object[] params = new Object[]{
				model.getWpId(),model.getDeviceId(),model.getIndicatorId(),model.getId()
		};
		int check = 0;
		try {
			check = this.queryForInt(checkSql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("唯一性验证时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("违反唯一性原则");
			return result;
		}
		//拼接修改语句
		StringBuffer sql = new StringBuffer();
		sql.append("update aiot_firstpage_show set wpid=?,deviceid=?,indicatorid=? where id =?");
		Object[] param = new Object[]{
				model.getWpId(),model.getDeviceId(),model.getIndicatorId(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
		}
		return result;
	}
	
	/*
	 *  新增修改首页实时数据管理
	 */
	public Result saveNewFirstPageShow(FirstPageShow model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//拼接修改语句
		StringBuffer sql = new StringBuffer();
		sql.append("insert into aiot_firstpage_show(wpid,deviceid,indicatorid) values(?,?,?) on duplicate key update indicatorid = values(indicatorid)");
		Object[] param = new Object[]{
				model.getWpId(),model.getDeviceId(),model.getIndicatorId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
		}
		return result;
	}
	
	/*
	 * 删除首页实时数据配置
	 */
	public Result deleFirstPageShowSetting(FirstPageShow model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		//拼接修改语句
		StringBuffer sql = new StringBuffer();
		sql.append("delete from aiot_firstpage_show where id in (").append(model.getIds()).append(")");
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	
	//根据站点获得首页展示的系统运行状态
	public List<SystemModel> getSystemModels(StationModel station){
			//初始化时间参数
			DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			//获得当前时间三天内的数据
			calendar.add(Calendar.DATE, -3);
			String beginDate = beginDf.format(calendar.getTime());
			List<SystemModel> list = new ArrayList<SystemModel>();
			StringBuffer sql = new StringBuffer("");
			sql.append(" select u.indicatorcode,u.collecttime,u.indicatorname,u.unitlogo,");
			sql.append(" case when u.type = 1 then FORMAT(u.data,2) when u.type = 2 then e.value end as data");
			sql.append(" from (");
			sql.append(" select a.indicator_code as indicatorcode,max(collect_time) as collectTime, a.title as indicatorName, ");
			sql.append(" a.type,a.data,");
			sql.append(" case when a.logo is null then '' else a.logo end as unitlogo ");
			sql.append(" from (");
			sql.append(" select m.indicator_code,m.collect_time,n.title,m.data,g.logo,b.type,b.ordercode as showOrder,n.ordercode as dmOrder");
			sql.append(" from aiot_metadata_system m,dm_indicator n left join g_unit g on n.unitid = g.id, aiot_firstpage_system_show b ");
			sql.append(" where m.indicator_code = n.code and m.wpid =").append(station.getId());
			sql.append(" and m.indicator_code = b.indicatorcode and n.isactive = 1");
			sql.append(" and m.collect_time >= '").append(beginDate).append("'");
			sql.append(" order by m.collect_time desc) a");
			sql.append(" group by a.indicator_code  order by a.showOrder,a.dmOrder");
			sql.append(" ) u left join aiot_firstpage_system_config e on  CONVERT(u.data,SIGNED) =e.key");
			list = this.queryObjectList(sql.toString(), SystemModel.class);
			return list;
	}
	
	//根据站点获得首页展示的传感器状态信息
	public List<SystemModel> getChuanSystemModels(StationModel station){
		//初始化时间参数
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//获得当前时间三天内的数据
		calendar.add(Calendar.DATE, -3);
		String beginDate = beginDf.format(calendar.getTime());
		
		List<SystemModel> list = new ArrayList<SystemModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.wpid as stationId,a.deviceid,a.indicator_code as indicatorcode,max(collect_time) as collectTime, a.name as indicatorName,");
		sql.append(" case when a.type = 1 then FORMAT(a.data,2) when a.type = 2 then e.value end as data, ''  as unitlogo");
		sql.append(" from (");
		sql.append(" select m.wpid,m.indicator_code,m.collect_time,c.name,b.type,m.data,b.ordercode as showOrder,c.ordercode as deorder,c.id as deviceId");
		sql.append(" from aiot_metadata_system m,aiot_firstpage_system_show b,device_catalog c");
		sql.append(" where m.indicator_code = b.indicatorcode");
		sql.append(" and b.indicatorcode = c.code and m.wpid = ").append(station.getId());
		sql.append(" and m.collect_time >= '").append(beginDate).append("'");
		sql.append(" order by m.collect_time desc) a");
		sql.append(" left join aiot_firstpage_system_config e on  CONVERT(a.data,SIGNED) =e.key");
		sql.append(" group by a.indicator_code");
		sql.append(" order by a.showOrder,a.deorder");
		list = this.queryObjectList(sql.toString(), SystemModel.class);
		return list;
	}
	
	
	//根据条件查询出需要在首页展示的系统运行状态
	public List<SystemModel> getSystemModelList(SystemModel model){
		
		//读取面板温度，供电电压，网络状态列表
		List<SystemModel> list = new ArrayList<SystemModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.wpid as stationid,c.title as stationName,a.indicatorcode,b.title as indicatorName,a.type,a.orderCode,");
		sql.append(" case a.type when 1 then '数值类型' when 2 then '布尔类型' else '' end as typeName");
		sql.append(" from aiot_firstpage_system_show a,dm_indicator b ,aiot_watch_point c");
		sql.append(" where a.indicatorcode = b.code");
		sql.append(" and b.isactive = 1");
		sql.append(" and a.wpid = c.id and c.isactive = 1");
		//添加查询条件
		if(model!=null&&model.getStationId()>10000){
			sql.append(" and a.wpid = ").append(model.getStationId());
		}
		//增加排序
		sql.append(" order by c.ordercode, a.orderCode ");
		list = this.queryObjectList(sql.toString(), SystemModel.class);
		
		//读取传感器参数列表
		List<SystemModel> clist = new ArrayList<SystemModel>();
		StringBuffer csql = new StringBuffer("");
		csql.append(" select a.id,a.wpid as stationid,c.title as stationName,a.indicatorcode,b.name as indicatorName,a.type,a.orderCode,");
		csql.append(" case a.type when 1 then '数值类型' when 2 then '布尔类型' else '' end as typeName");
		csql.append(" from aiot_firstpage_system_show a,device_catalog b ,aiot_watch_point c");
		csql.append(" where a.indicatorcode = b.code");
		csql.append(" and a.wpid = c.id and c.isactive = 1");
		//添加查询条件
		if(model!=null&&model.getStationId()>10000){
			csql.append(" and a.wpid = ").append(model.getStationId());
		}
		//增加排序
		csql.append(" order by c.ordercode, a.orderCode ");
		clist = this.queryObjectList(csql.toString(), SystemModel.class);
		list.addAll(clist);
		return list;
	}
	//增加在首页展示的系统运行状态
	public Result addSystemModel(SystemModel model){
		//初始化返回信息
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//判断是否有重复数据
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_firstpage_system_show where wpid =").append(model.getStationId()).append(" and indicatorcode ='").append(model.getIndicatorCode()).append("'");
		int check=0;
		try {
			check = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("唯一性监测时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("违反唯一性原则");
			return result;
		}
		//开始加入数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_firstpage_system_show(wpid,indicatorcode,type,orderCode) values(?,?,?,?) on duplicate key update type=values(type),orderCode=values(orderCode)");
		Object[] params = new Object[]{
				model.getStationId(),model.getIndicatorCode(),model.getType(),model.getOrderCode()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
		}
		return result;
	}
	//修改在首页展示的系统运行状态
	public Result updateSystemModel(SystemModel model){
		//初始化返回信息
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//判断是否有重复数据
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_firstpage_system_show where wpid =").append(model.getStationId()).append(" and indicatorcode ='").append(model.getIndicatorCode()).append("'");
		checkSql.append(" and id <>").append(model.getId());
		int check=0;
		try {
			check = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("唯一性监测时失败");
			return result;
		}
		if(check>0){
			result.setResult(result.FAILED);
			result.setMessage("违反唯一性原则");
			return result;
		}
		//开始加入数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_firstpage_system_show set wpid=?,indicatorcode=?,type=?,orderCode=? where id =?");
		Object[] params = new Object[]{
				model.getStationId(),model.getIndicatorCode(),model.getType(),model.getOrderCode(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("修改失败");
		}
		return result;
	}
	//删除在首页展示的系统运行状态
	public Result deleSystemModel(SystemModel model){
		//初始化返回参数
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		//开始删除数据
		StringBuffer sql = new StringBuffer("");
		sql.append("delete from aiot_firstpage_system_show where id = ").append(model.getId());
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	
	/*
	 * 根据站点获得该站点需要展示的系统运行状态的参数列表以及传感器列表
	 */
	public List<IndicatorModel> getSystemModelList4Set(StationModel station){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		//获得该站点内的需要展示的参数列表
		StringBuffer indisql = new StringBuffer("");
		indisql.append(" select a.code,a.title");
		indisql.append(" from dm_indicator a,dm_indicator_group b");
		indisql.append(" where a.groupid = b.id");
		indisql.append(" and a.isactive = 1 and b.id = 3");
		List<IndicatorModel> inlist = new ArrayList<IndicatorModel>();
		inlist = this.queryObjectList(indisql.toString(), IndicatorModel.class);
		//获得该站点内的需要展示的传感器的列表
		StringBuffer desql=new StringBuffer("");
		desql.append(" select a.code,concat(a.name,'(传感器)') as title");
		desql.append(" from device_catalog a,aiot_station_device_comm b");
		desql.append(" where a.id = b.deviceid");
		desql.append(" and a.code <> 'System'");
		desql.append(" and b.stationid =").append(station.getId());
		List<IndicatorModel> delist = new ArrayList<IndicatorModel>();
		delist = this.queryObjectList(desql.toString(), IndicatorModel.class);
		list.addAll(inlist);
		list.addAll(delist);
		return list;
	}
}
