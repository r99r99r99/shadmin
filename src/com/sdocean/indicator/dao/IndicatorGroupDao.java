package com.sdocean.indicator.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorGroupModel;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.role.model.RoleModel;
import com.sdocean.users.model.SysUser;

@Component
public class IndicatorGroupDao extends OracleEngine{
	
	/*
	 * 查询符合条件的参数组
	 */
	public List<IndicatorGroupModel> showIndicatorGroups(IndicatorGroupModel model){
		List<IndicatorGroupModel> list = new ArrayList<IndicatorGroupModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.title,a.picture,a.description,a.isactive,b.value as isactivename");
		sql.append(" from dm_indicator_group a ");
		sql.append(" left join sys_public b on b.parentcode = '0004' and a.isactive = b.classid");
		sql.append(" where 1 = 1");
		//添加查询条件
		if(model!=null&&model.getCode()!=null&&model.getCode().length()>0){
			sql.append(" and  ( a.code like '%").append(model.getCode()).append("%' or");
			sql.append(" a.title like '%").append(model.getCode()).append("%')");
		}
		if(model!=null&&model.getIsactive()<2){
			sql.append(" and a.isactive = ").append(model.getIsactive());
		}
		list = this.queryObjectList(sql.toString(), IndicatorGroupModel.class);
		return list;
	}
	
	/*
	 * 保存修改的参数组
	 */
	public Result saveGroupChange(IndicatorGroupModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//监测CODE是否唯一
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from dm_indicator_group where id <>").append(model.getId()).append(" and code = '").append(model.getCode()).append("'");
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("监测CODE唯一时执行失败!");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("CODE违反唯一性原则");
			return result;
		}
		//执行修改代码
		StringBuffer sql = new StringBuffer("");
		sql.append(" update dm_indicator_group set code=?,title=?,picture=?,description=?,isactive=? where id =?");
		Object[] params = new Object[]{
				model.getCode(),model.getTitle(),model.getPicture(),
				model.getDescription(),model.getIsactive(),model.getId()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败!");
			// TODO: handle exception
		}
		
		return result;
	}
	
	/*
	 * 保存新增的参数组
	 */
	public Result saveNewGroup(IndicatorGroupModel model){
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//监测CODE是否唯一
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from dm_indicator_group where code = '").append(model.getCode()).append("'");
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("监测CODE唯一时执行失败!");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("CODE违反唯一性原则");
			return result;
		}
		//执行修改代码
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into dm_indicator_group(code,title,picture,description,isactive) values(?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getCode(),model.getTitle(),model.getPicture(),
				model.getDescription(),model.getIsactive()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败!");
			// TODO: handle exception
		}
		
		return result;
	}
	
	/*
	 * 删除选中的参数组
	 */
	public Result deleGroup(String ids){
		//初始化返回值
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(ids));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" update dm_indicator_group set isactive = 0 where id in (").append(ids).append(")");
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败!");
		}
		
		return result;
	} 
}
