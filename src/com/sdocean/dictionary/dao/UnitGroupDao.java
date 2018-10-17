package com.sdocean.dictionary.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.UnitGroupModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;

@Component
public class UnitGroupDao extends OracleEngine {
	
	/*
	 * 得到所有的单位组的列表
	 */
	public List<UnitGroupModel> getUnitGroups(UnitGroupModel model){
		List<UnitGroupModel> list = new ArrayList<UnitGroupModel>();
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.description");
		sql.append(" from g_unit_group a ");
		//增加查询条件
		//增加模糊查询条件
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and ( a.code like '%").append(model.getCode()).append("%' or ");
			sql.append(" a.name like '%").append(model.getCode()).append("%' )");
		}
		list = this.queryObjectList(sql.toString(), UnitGroupModel.class);
		return list;
	}
	
	/*
	 * 保存单位组的修改
	 */
	public Result saveGroupChange(UnitGroupModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" update g_unit_group set code=?,name=?,description=? where id =?");
		Object[] params = new Object[]{
			model.getCode(),model.getName(),model.getDescription(),
			model.getId()
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
	
	/*
	 * 保存单位组的创建
	 */
	public Result saveNewGroup(UnitGroupModel model){
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into g_unit_group(code,name,description) values(?,?,?)");
		Object[] params = new Object[]{
			model.getCode(),model.getName(),model.getDescription()
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
	
	/*
	 * 获得有效的单位组的集合
	 */
	public List<UnitGroupModel> getUnitGroups(){
		List<UnitGroupModel> list = new ArrayList<UnitGroupModel>();
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.description");
		sql.append(" from g_unit_group a ");
		list = this.queryObjectList(sql.toString(), UnitGroupModel.class);
		return list;
	}
	
}
