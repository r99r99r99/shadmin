package com.sdocean.station.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;

@Component
public class StationTypeDao extends OracleEngine {
	
	/*
	 * 为站点类型展示结果查询结果集
	 */
	public List<StationTypeModel> getTypeList(StationTypeModel type){
		List<StationTypeModel> res = new ArrayList<StationTypeModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.remark,a.isactive,b.value as iactiveName");
		sql.append(" from g_station_type a ");
		sql.append(" left join sys_public b on a.isactive = b.classid and b.parentcode = '0004'");
		sql.append(" where  1 = 1");
		//添加查询条件
		//code 或 name 模糊查询
		if(type!=null&&type.getCode()!=null&&type.getCode().length()>0){
			sql.append(" and (code like '%").append(type.getCode()).append("%' or name like '%").append(type.getCode()).append("%')");
		}
		if(type!=null&&type.getIsactive()<2){
			sql.append(" and a.isactive = ").append(type.getIsactive());
		}
		res = this.queryObjectList(sql.toString(), StationTypeModel.class);
		return res;
	}
	
	/*
	 * 保存站点类型修改
	 */
	public Result saveTypeChange(StationTypeModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功!");
		//判断code是否重复
		StringBuffer check = new StringBuffer("");
		check.append(" select count(1) from g_station_type");
		check.append(" where code = '").append(model.getCode()).append("'");
		check.append(" and id <>").append(model.getId());
		int count = this.queryForInt(check.toString(), null);
		if(count>0){
			result.setResult(result.FAILED);
			result.setMessage("保存失败,类型名称重复.");
			return result;
		}
		//将结果保存到数据库中
		StringBuffer sql = new StringBuffer();
		sql.append(" update g_station_type set code = ?,name =?,remark=?,isactive =? where id =?");
		Object[] params = new Object[]{
				model.getCode(),model.getName(),model.getRemark(),model.getIsactive(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败,请重试");
			return result;
		}
		return result;
	}
	
	/*
	 * 新增站点类型保存
	 */
	public Result saveNewType(StationTypeModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setMessage("新增成功");
		result.setResult(result.SUCCESS);
		//判断CODE是否重复
		StringBuffer check = new StringBuffer("");
		check.append(" select count(1) from g_station_type where code = '").append(model.getCode()).append("'");
		int count = this.queryForInt(check.toString(), null);
		if(count>0){
			result.setResult(result.FAILED);
			result.setMessage("新增失败,类型名称重复");
			return result;
		}
		//将结果保存到数据库中
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into g_station_type(code,name,isactive,remark) values(?,?,?,?)");
		Object[] params = new Object[]{
			model.getCode(),model.getName(),model.getIsactive(),model.getRemark()	
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败,请重试");
			return result;
		}
		return result;
	}
}
