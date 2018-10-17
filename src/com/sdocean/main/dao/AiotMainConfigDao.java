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
public class AiotMainConfigDao extends OracleEngine{
	
	/*
	 * 获得例行维护分类列表
	 */
	public List<AiotMainConfigModel> getAiotMainConfigList(AiotMainConfigModel model){
		List<AiotMainConfigModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.how,a.ordercode ");
		sql.append(" from aiot_main_config a");
		sql.append(" where 1= 1 ");
		//添加查询条件
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and code like '%").append(model.getCode()).append("%'");
		}
		//添加排序
		sql.append(" order by ordercode");
		list = this.queryObjectList(sql.toString(), AiotMainConfigModel.class);
		return list;
	}
	/*
	 * 新增例行维护分类
	 */
	public Result saveNewMainConfig(AiotMainConfigModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		//判断CODE是否重复
		int res = 0;
		StringBuffer csql = new StringBuffer("");
		csql.append(" select count(1) from aiot_main_config where code = '").append(model.getCode()).append("'");
		try {
			res = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("唯一验证时失败");
			return result;
		}
		if(res >0){
			result.setResult(Result.FAILED);
			result.setMessage("已有相同的CODE");
			return result;
		}
		//插入数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_main_config(code,name,how,ordercode)");
		sql.append(" values(?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getName(),model.getHow(),
				model.getOrderCode()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("插入数据失败");
			return result;
		}
		return result;
	}
	/*
	 * 修改例行维护分类
	 */
	public Result saveChangeMainConfig(AiotMainConfigModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		//判断CODE是否重复
		int res = 0;
		StringBuffer csql = new StringBuffer("");
		csql.append(" select count(1) from aiot_main_config where code = '").append(model.getCode()).append("' and id <> ").append(model.getId());
		try {
			res = this.queryForInt(csql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(Result.FAILED);
			result.setMessage("唯一验证时失败");
			return result;
		}
		if(res >0){
			result.setResult(Result.FAILED);
			result.setMessage("已有相同的CODE");
			return result;
		}
		//修改数据
		StringBuffer sql = new StringBuffer("");
		sql.append(" update aiot_main_config set code=?,name=?,how=?,ordercode=? where id=?");
		Object[] params = new Object[]{
				model.getCode(),model.getName(),model.getHow(),
				model.getOrderCode(),model.getId()
		};
		try {
			this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("修改数据失败");
			return result;
		}
		return result;
	}
	/*
	 *  删除例行维护分类
	 */
	public Result deleMainConfig(AiotMainConfigModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.DELETE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("删除成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from  aiot_main_config where id =").append(model.getId());
		try {
			this.update(sql.toString(), null);
		} catch (Exception e) {
			// TODO: handle exception
			result.setResult(Result.FAILED);
			result.setMessage(" 数据失败");
			return result;
		}
		return result;
	}
}
