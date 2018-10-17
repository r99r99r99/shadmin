package com.sdocean.dataQuery.dao;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.dataQuery.model.MouldModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.station.model.StationModel;

@Component
public class MouldDao extends OracleEngine{
	
	/*
	 * 获得当前站点的水质评价模板
	 */
	public MouldModel getMouldByStationId(MouldModel model){
		MouldModel result = new MouldModel();
		StringBuffer sql = new StringBuffer("");
		sql.append("select id,stationid,mould from sys_mouldsetting where stationid = ").append(model.getStationId());
		result = this.queryObject(sql.toString(), MouldModel.class);
		if(result==null){
			result = new MouldModel();
			result.setStationId(model.getStationId());
		}
		return result;
	}
	/*
	 * 保存当前站点的水质评价模板
	 */
	public Result saveMouldSetting(MouldModel model){
		//初始化返回结果
		Result result = new Result();
		if(model.getId()>0){
			result.setDotype(result.UPDATE);
		}else{
			result.setDotype(result.ADD);
		}
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//开始拼接插入/修改语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_mouldsetting(stationid,mould) values(?,?) on duplicate key update mould=values(mould)");
		Object[] params = new Object[]{
				model.getStationId(),model.getMould()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
		}
		return result;
	}
	
	/*
	 * 根据站点,获得水质评价模板
	 */
	public MouldModel getMouldByStation(StationModel model){
		MouldModel result = new MouldModel();
		StringBuffer sql = new StringBuffer("");
		sql.append("select id,stationid,mould from sys_mouldsetting where stationid = ").append(model.getId());
		result = this.queryObject(sql.toString(), MouldModel.class);
		if(result==null){
			result = new MouldModel();
			result.setStationId(model.getId());
		}
		return result;
	}
}
