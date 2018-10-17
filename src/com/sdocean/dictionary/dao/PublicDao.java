package com.sdocean.dictionary.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.role.model.RoleModel;
import com.sdocean.users.model.SysUser;

@Component
public class PublicDao extends OracleEngine{
	
	/*
	 * 通过parentCode查找
	 */
	public List<PublicModel> getPublicsByParent(String parentCode){
		List<PublicModel> publics = new ArrayList<PublicModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select distinct a.id,a.parentcode,a.classid,a.classcode,a.classname,a.value,a.remark");
		sql.append(" from sys_public a where a.parentcode = '").append(parentCode).append("'");
		sql.append(" order by a.classid");
		publics = this.queryObjectList(sql.toString(), PublicModel.class);
		return publics;
	}
	
	/*
	 * 查询符合条件的公共代码
	 */
	public List<PublicModel> getPublics(PublicModel model){
		List<PublicModel> list = new ArrayList<PublicModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.parentCode,a.classId,a.classCode,a.className,a.value,a.remark,a.orderCode from sys_public a");
		sql.append(" where 1=1");
		//增加模糊查询条件
		if(model!=null&&model.getParentCode()!=null&&model.getParentCode().length()>0){
			sql.append(" and (");
			sql.append(" a.parentCode like '%").append(model.getParentCode()).append("%' or");
			sql.append(" a.classname like '%").append(model.getParentCode()).append("%' or");
			sql.append(" a.value like '%").append(model.getParentCode()).append("%' ");
			sql.append(" )");
		}
		//增加排序
		sql.append(" order by a.parentCode");
		list = this.queryObjectList(sql.toString(), PublicModel.class);
		return list;
	}
	
	/*
	 * 保存修改的公共代码
	 */
	public Result savePublicChange(PublicModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//验证parentCode  classid 是否唯一
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from sys_public");
		checkSql.append(" where id <> ").append(model.getId());
		checkSql.append(" and parentcode = '").append(model.getParentCode()).append("'");
		checkSql.append(" and classid = ").append(model.getClassId());
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("验证是否标准失败");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("parentCode与ClassId违反了唯一性原则");
			return result;
		}
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" update sys_public set parentCode=?,classId=?,classCode=?,className=?,value=?,remark=?,orderCode=? where id=?");
		Object[] params = new Object[]{
				model.getParentCode(),model.getClassId(),model.getClassCode(),model.getClassName(),
				model.getValue(),model.getRemark(),model.getOrderCode(),model.getId()
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
	 * 保存新增的公共代码
	 */
	public Result saveNewPublic(PublicModel model){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(result.SUCCESS);
		result.setMessage("修改成功");
		//验证parentCode,classId是否重复
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from sys_public");
		checkSql.append(" where parentcode = '").append(model.getParentCode()).append("'");
		checkSql.append(" and classid = ").append(model.getClassId());
		int cou = 0;
		try {
			cou = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("验证是否标准失败");
			return result;
		}
		if(cou>0){
			result.setResult(result.FAILED);
			result.setMessage("parentCode与ClassId违反了唯一性原则");
			return result;
		}
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_public(parentCode,classId,classCode,className,value,remark,orderCode) values(?,?,?,?,?,?,?)");
		Object[] params = new Object[]{
				model.getParentCode(),model.getClassId(),model.getClassCode(),model.getClassName(),
				model.getValue(),model.getRemark(),model.getOrderCode()
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
	 * 删除选中的公共代码
	 */
	public Result delePublic(String ids){
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(ids));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from sys_public where id in (").append(ids).append(")");
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		return result;
	}
	 
}
