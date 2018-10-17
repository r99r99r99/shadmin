package com.sdocean.main.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.main.model.AiotMainConfigModel;
import com.sdocean.main.model.StationDeviceMainModel;
import com.sdocean.station.model.StationModel;

@Component
public class StationDeviceMainDao extends OracleEngine{
	
	/*
	 * 查询条件范围内的站点设备维护配置列表
	 */
	public List<StationDeviceMainModel> getStationDeviceMainList(StationDeviceMainModel model,List<StationModel> stations){
		List<StationDeviceMainModel> list = new ArrayList<StationDeviceMainModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.madcid,c.id as stationid,c.title as stationName,");
		sql.append(" d.id as deviceid,d.name as deviceName,b.createtime,");
		sql.append(" a.amcid as amconfigid,e.code as mainConfigCode,e.name as mainConfigName,");
		sql.append(" a.mainnum,e.how");
		sql.append(" from aiot_madc_amc a,map_awp_device_catalog b,");
		sql.append(" aiot_watch_point c,device_catalog d,aiot_main_config e");
		sql.append(" where a.madcid = b.id and a.amcid = e.id");
		sql.append(" and b.aiot_watch_point_id = c.id ");
		sql.append(" and b.device_catalog_id = d.id");
		//添加查询条件
		if(model!=null&&model.getStationId()>0){
			sql.append(" and c.id = ").append(model.getStationId());
		}else{
			StringBuffer ssql = new StringBuffer("");
			ssql.append(" (0");
			for(StationModel station:stations){
				ssql.append(",").append(station.getId());
			}
			ssql.append(")");
			sql.append(" and c.id in ").append(ssql);
		}
		sql.append(" order by c.id,b.orderCode");
		list = this.queryObjectList(sql.toString(), StationDeviceMainModel.class);
		return list;
	}
	/*
	 * 保存新增的站点设备维护配置
	 */
	public Result saveStationDeviceMain(StationDeviceMainModel model){
		//初始化返回列表
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		//判断唯一性原则
		StringBuffer csql = new StringBuffer("");
		int cres = 0;
		csql.append(" select count(1) from aiot_madc_amc where madcid = ").append(model.getMadcId()).append(" and amcid =").append(model.getAmconfigId());
		try {
			cres = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("唯一性原则检查时失败");
			return result;
		}
		if(cres>0){
			result.setResult(Result.FAILED);
			result.setMessage("违法唯一性原则");
			return result;
		}
		//开始添加站点设备维护配置
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_madc_amc(madcid,amcid,mainnum) values(?,?,?)");
		Object[] params = new Object[]{
				model.getMadcId(),model.getAmconfigId(),model.getMainNum()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(Result.FAILED);
			result.setMessage("新增失败");
			return result;
		}
		return result;
	}
	/*
	 * 保存修改的站点设备维护配置
	 */
	public Result saveChangeStationDeviceMain(StationDeviceMainModel model){
		//初始化返回列表
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		//判断唯一性原则
		StringBuffer csql = new StringBuffer("");
		int cres = 0;
		csql.append(" select count(1) from aiot_madc_amc where madcid = ").append(model.getMadcId()).append(" and amcid =").append(model.getAmconfigId());
		csql.append(" and id <> ").append(model.getId());
		try {
			cres = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("唯一性原则检查时失败");
			return result;
		}
		if(cres>0){
			result.setResult(Result.FAILED);
			result.setMessage("违法唯一性原则");
			return result;
		}
		
		//开始添加站点设备维护配置
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_madc_amc set madcid=?,amcid=?,mainnum=?, where id=?");
		Object[] params = new Object[]{
				model.getMadcId(),model.getAmconfigId(),model.getMainNum(),model.getId()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(Result.FAILED);
			result.setMessage("修改失败");
			return result;
		}
		return result;
	}
	/*
	 * 删除选中的站点设备维护配置
	 */
	public Result deleStationDeviceMain(StationDeviceMainModel model){
		//初始化返回列表
		Result result = new Result();
		result.setDotype(Result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer("");
		sql.append("delete from aiot_madc_amc where id = ").append(model.getId());
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
	 * 根据站点和设备获得有效的例行维护种类的列表
	 */
	public List<AiotMainConfigModel> getAiotMainConfigListByStationDevice(StationDeviceMainModel model){
		List<AiotMainConfigModel> list = new ArrayList<AiotMainConfigModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select b.id,b.code,b.name,b.how,b.ordercode");
		sql.append(" from aiot_madc_amc a,aiot_main_config b,map_awp_device_catalog c");
		sql.append(" where a.madcid = c.id and a.amcid = b.id ");
		sql.append(" and c.aiot_watch_point_id =").append(model.getStationId()).append(" and c.device_catalog_id = ").append(model.getDeviceId());
		sql.append(" order by b.ordercode");
		list = this.queryObjectList(sql.toString(), AiotMainConfigModel.class);
		return list;
	}
}
