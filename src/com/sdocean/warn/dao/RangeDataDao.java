package com.sdocean. warn.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.station.dao.StationDao;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.warn.model.RangeDataModel;
import com.sdocean.warn.model.Warn4FirstModel;
import com.sdocean.warn.model.WarnModel;
import com.sdocean.warn.model.WarnValueModel;

@Component
public class RangeDataDao extends OracleEngine {
	
	/**
	 * 得到查询条件下的站点的错误数据配置
	 */
	public List<RangeDataModel> getRangeDataList(RangeDataModel model){
		//初始化返回结果
		List<RangeDataModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,b.title as stationname,a.deviceid,c.name as devicename,");
		sql.append(" a.indicatorcode,d.title as indicatorname,a.minData,a.maxData");
		sql.append(" from aiot_range_data a,aiot_watch_point b,dm_indicator d,device_catalog c");
		sql.append(" where a.stationid = b.id and a.indicatorcode = d.code");
		sql.append(" and a.deviceid = c.id");
		sql.append(" and a.deviceid = c.id");
		//添加查询条件
		if(model!=null&&model.getStationId()>0){
			sql.append(" and a.stationid = ").append(model.getStationId());
		}
		//添加模糊查询条件
		if(model!=null&&model.getIndicatorCode()!=null&&model.getIndicatorCode().length()>0){
			sql.append(" and (d.code like '%").append(model.getIndicatorCode()).append("%'");
			sql.append(" or d.title like '%").append(model.getIndicatorCode()).append("%'");
			sql.append(" )");
		}
		//添加排序
		sql.append(" order by b.ordercode,d.ordercode");
		list = this.queryObjectList(sql.toString(), RangeDataModel.class);
		return list;
	}
	
	/*
	 * 新增错误数据配置
	 */
	public Result saveNewRangeData(RangeDataModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_range_data(stationid,indicatorcode,minData,maxData,deviceid)");
		sql.append(" values(?,?,?,?,?) on duplicate key update minData=values(minData),maxData=values(maxData)");
		Object[] param = new Object[]{
				model.getStationId(),model.getIndicatorCode(),model.getMinData(),model.getMaxData(),model.getDeviceId()
		};
		try {
			this.update(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
		}
		return result;
	}
	
	/*
	 * 修改错误数据配置
	 */
	public Result updateRangeData(RangeDataModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		StringBuffer sql = new StringBuffer();
		sql.append(" update aiot_range_data set stationid=?,indicatorcode=?,minData=?,maxData=?,deviceid=? where id=?");
		Object[] param = new Object[]{
				model.getStationId(),model.getIndicatorCode(),model.getMinData(),model.getMaxData(),model.getDeviceId(),model.getId()
		};
		try {
			this.update(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("新增失败");
		}
		return result;
	}
	
	/*
	 * 删除错误数据配置
	 */
	public Result deleRangeData(RangeDataModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer();
		sql.append(" delete from aiot_range_data where id=?");
		Object[] param = new Object[]{
				model.getId()
		};
		try {
			this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	
	/*
	 * 根据站点  参数查询出阈值范围
	 */
	public RangeDataModel getRangeDataByStationDeviceIndicator(int stationId,int deviceId,String indicatorCode){
		//初始化返回结果
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,b.title as stationname,a.deviceid,c.name as deviceName,");
		sql.append(" a.indicatorcode,d.title as indicatorname,a.minData,a.maxData");
		sql.append(" from aiot_range_data a,aiot_watch_point b,dm_indicator d,device_catalog c");
		sql.append(" where a.stationid = b.id and a.indicatorcode = d.code");
		sql.append(" and a.deviceid = c.id");
		sql.append(" and a.stationid = ").append(stationId);
		sql.append(" and a.deviceid = ").append(deviceId);
		sql.append(" and a.indicatorCode='").append(indicatorCode).append("'");
		sql.append(" limit 1");
		RangeDataModel ed = this.queryObject(sql.toString(), RangeDataModel.class);
		return ed;
	}
	
	/*
	 * 根据站点以及参数,获得该站点内所有能监测该参数的设备的量程范围
	 */
	public List<RangeDataModel> getRangeDataListByStationDeviceIndicator(int stationId,int deviceId,String indicatorCode){
		List<RangeDataModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.stationid,b.title as stationname,a.deviceid,c.name as deviceName,");
		sql.append(" a.indicatorcode,d.title as indicatorname,a.minData,a.maxData");
		sql.append(" from aiot_range_data a,aiot_watch_point b,dm_indicator d,device_catalog c");
		sql.append(" where a.stationid = b.id and a.indicatorcode = d.code");
		sql.append(" and a.deviceid = c.id");
		if(stationId>0) {
			sql.append(" and a.stationid = ").append(stationId);
		}
		if(deviceId>0) {
			sql.append(" and a.deviceid = ").append(deviceId);
		}
		if(indicatorCode!=null&&indicatorCode.length()>0) {
			sql.append(" and a.indicatorCode='").append(indicatorCode).append("'");
		}
		list = this.queryObjectList(sql.toString(), RangeDataModel.class);
		return list;
	}
}
