package com.sdocean.role.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;

@Component
public class RoleDao extends OracleEngine{
	
	/*
	 * 获得条件内的角色列表
	 */
	public List<RoleModel> getRoleList(RoleModel role){
		List<RoleModel> roles = new ArrayList<RoleModel>();
		//开始拼接SQL语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.name,a.remark,a.type,a.type,b.value as typename,a.isactive,c.value as isactivename");
		sql.append(" from sys_role a ");
		sql.append(" left join sys_public b on a.type = b.classid and b.parentcode = '0003'");
		sql.append(" left join sys_public c on a.isactive = c.classid and c.parentcode = '0004'");
		sql.append(" where 1=1");
		//添加查询条件
		if(role.getType()>0){
			sql.append(" and a.type =").append(role.getType());
		}
		roles = this.queryObjectList(sql.toString(), RoleModel.class);
		return roles;
	}
	
	/*
	 * 保存角色-人员-菜单
	 * 
	 */
	public Result saveRoleUserMenu(RoleModel role){
		Result result = new Result();
		//设置操作类型
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(role));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//获得当前角色id
		int roleId = role.getId();
		//获得选中的人员列表
		String userIds = role.getUserIds();
		String[] users = userIds.split(",");
		//获得选中的菜单列表
		String menuIds = role.getMenuIds();
		String[] menus = menuIds.split(",");
		//删除原有权限
		StringBuffer deleUserSql = new StringBuffer("");
		deleUserSql.append(" delete from sys_role_user where role_id = ").append(roleId);
		this.execute(deleUserSql.toString());
		//保存人员关联角色部分
		StringBuffer userSql = new StringBuffer("");
		StringBuffer valueSql = new StringBuffer("(0,0)");
		for(int i=0;i<users.length;i++){
			valueSql.append(",(").append(roleId).append(",").append(users[i]).append(")");
		}
		userSql.append(" insert into sys_role_user(role_id,user_id)");
		userSql.append(" values ").append(valueSql).append(" ON DUPLICATE KEY UPDATE user_id = user_id");
		try {
			this.execute(userSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		//删除原有权限部分
		StringBuffer delemenuSql = new StringBuffer("");
		delemenuSql.append("delete from sys_role_menu where role_id = ").append(roleId);
		this.execute(delemenuSql.toString());
		//保存菜单关联角色部分
		StringBuffer menuSql = new StringBuffer("");
		StringBuffer valuemenuSql = new StringBuffer("(0,0)");
		for(int i=0;i<menus.length;i++){
			valuemenuSql.append(",(").append(roleId).append(",'").append(menus[i]).append("')");
		}
		menuSql.append(" insert into sys_role_menu(role_id,menu_id)");
		menuSql.append(" values ").append(valuemenuSql).append(" ON DUPLICATE KEY UPDATE menu_id = menu_id");
		try {
			this.execute(menuSql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		return result;
	}
	
	/*
	 * 保存角色-人员-站点
	 * 
	 */
	public Result saveRoleUserStation(RoleModel role){
		Result result = new Result();
		//设置操作类型
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(role));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//获得当前角色id
		int roleId = role.getId();
		//获得选中的人员列表
		String userIds = role.getUserIds();
		String[] users = userIds.split(",");
		//获得选中的菜单列表
		String menuIds = role.getStationIds();
		String[] menus = menuIds.split(",");
		//删除原有权限
		StringBuffer deleUserSql = new StringBuffer("");
		deleUserSql.append(" delete from sys_role_user where role_id = ").append(roleId);
		this.execute(deleUserSql.toString());
		//保存人员关联角色部分
		StringBuffer userSql = new StringBuffer("");
		StringBuffer valueSql = new StringBuffer("(0,0)");
		for(int i=0;i<users.length;i++){
			valueSql.append(",(").append(roleId).append(",").append(users[i]).append(")");
		}
		userSql.append(" insert into sys_role_user(role_id,user_id)");
		userSql.append(" values ").append(valueSql).append(" ON DUPLICATE KEY UPDATE user_id = user_id");
		try {
			this.execute(userSql.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		//删除原有权限部分
		StringBuffer delemenuSql = new StringBuffer("");
		delemenuSql.append("delete from sys_role_station where role_id = ").append(roleId);
		this.execute(delemenuSql.toString());
		//保存菜单关联角色部分
		StringBuffer menuSql = new StringBuffer("");
		StringBuffer valuemenuSql = new StringBuffer("(0,0)");
		for(int i=0;i<menus.length;i++){
			valuemenuSql.append(",(").append(roleId).append(",").append(menus[i]).append(")");
		}
		menuSql.append(" insert into sys_role_station(role_id,station_id)");
		menuSql.append(" values ").append(valuemenuSql).append(" ON DUPLICATE KEY UPDATE station_id = station_id");
		try {
			this.execute(menuSql.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		return result;
	}
	
	/*
	 * 新增角色
	 */
	public Result saveAddRole(RoleModel role){
		Result result = new Result();
		result.setDotype(result.ADD);
		result.setModel(JsonUtil.toJson(role));
		result.setResult(result.SUCCESS);
		result.setMessage("新增成功");
		//验证是否有重名情况
		StringBuffer check = new StringBuffer("");
		check.append("select count(1) from sys_role where name = '").append(role.getName()).append("'");
		int res = this.queryForInt(check.toString(), null);
		if(res>0){
			result.setResult(result.FAILED);
			result.setMessage("角色名称重复,请重试");
			return result;
		}
		
		//进行保存操作
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into sys_role(name,type,isactive,remark) values(?,?,?,?)");
		Object[] param = new Object[]{
			role.getName(),role.getType(),role.getIsactive(),role.getRemark()	
		};
		int insert = 0;
		try {
			insert = this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("新增失败,请重试");
			return result;
		}
		return result;
	}
	
	/*
	 * 保存编辑的角色
	 */
	public Result saveEditRole(RoleModel role){
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(role));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//验证更改后是否有重名的情况
		StringBuffer check = new StringBuffer("");
		check.append(" select count(1) from sys_role where id <>").append(role.getId()).append(" and name = '").append(role.getName()).append("'");
		int res = this.queryForInt(check.toString(), null);
		if(res>0){
			result.setResult(result.FAILED);
			result.setMessage("角色名称重复,请重新输入");
			return result;
		}
		//进行保存操作
		StringBuffer sql = new StringBuffer("");
		sql.append("update sys_role set name = ?,type=?,isactive = ?,remark=? where id = ?");
		Object[] param = new Object[]{
			role.getName(),role.getType(),role.getIsactive(),role.getRemark(),role.getId()	
		};
		int update = 0;
		try {
			update = this.update(sql.toString(), param);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败,请重试");
			return result;
		}
		return result;
	}
	
	/*
	 * 删除选中的角色
	 */
	public Result deleRole(RoleModel role){
		Result result = new Result();
		result.setDotype(result.DELETE);
		result.setModel(JsonUtil.toJson(role));
		result.setResult(result.SUCCESS);
		result.setMessage("删除成功");
		
		//删除角色
		StringBuffer rolesql = new StringBuffer("");
		rolesql.append("delete from sys_role where id = ").append(role.getId());
		try {
			this.update(rolesql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
			return result;
		}
		
		//删除菜单权限
		StringBuffer menusql = new StringBuffer("");
		menusql.append("delete from sys_role_user where role_id = ").append(role.getId());
		try {
			this.update(menusql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		//删除人员权限
		StringBuffer usersql = new StringBuffer("");
		usersql.append("delete from sys_role_user where role_id = ").append(role.getId());
		try {
			this.update(usersql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		//删除站点权限
		StringBuffer stationsql = new StringBuffer("");
		stationsql.append("delete from sys_role_user where role_id = ").append(role.getId());
		try {
			this.update(stationsql.toString(), null);
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除失败");
		}
		
		return result;
	}
	
	/*
	 * 保存首页角色权限
	 */
	public Result saveFirstMenuRole(RoleModel role){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(result.UPDATE);
		result.setModel(JsonUtil.toJson(role));
		result.setResult(result.SUCCESS);
		result.setMessage("保存成功");
		//获得当前角色id
		int roleId = role.getId();
		//获得选中的人员列表
		String userIds = role.getUserIds();
		String[] users = userIds.split(",");
		//获得选中的菜单列表
		String menuIds = role.getMenuIds();
		//处理角色关联人员部分
		//删除原有权限
		StringBuffer deleUserSql = new StringBuffer("");
		deleUserSql.append(" delete from sys_role_user where role_id = ").append(roleId);
		this.execute(deleUserSql.toString());
		//保存人员关联角色部分
		StringBuffer userSql = new StringBuffer("");
		StringBuffer valueSql = new StringBuffer("(0,0)");
		for(int i=0;i<users.length;i++){
			valueSql.append(",(").append(roleId).append(",").append(users[i]).append(")");
		}
		userSql.append(" insert into sys_role_user(role_id,user_id)");
		userSql.append(" values ").append(valueSql).append(" ON DUPLICATE KEY UPDATE user_id = user_id");
		try {
			this.execute(userSql.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("保存失败");
			return result;
		}
		//处理角色关联菜单部分
		//删除原有权限部分
		StringBuffer deleMenuSql = new StringBuffer("");
		deleMenuSql.append(" delete from sys_role_firstmenu where role_id = ").append(roleId);
		try {
			this.execute(deleMenuSql.toString());
		} catch (Exception e) {
			result.setResult(result.FAILED);
			result.setMessage("删除首页菜单角色时失败!");
			return result;
		}
		if(menuIds!=null&&menuIds.length()>0){
			StringBuffer addMenuSql = new StringBuffer("");
			addMenuSql.append(" insert into sys_role_firstmenu(role_id,menu_id) values(").append(roleId).append(",");
			addMenuSql.append(" '").append(menuIds).append("') ON DUPLICATE KEY UPDATE menu_id=values(menu_id)");
			try {
				this.execute(addMenuSql.toString());
			} catch (Exception e) {
				result.setResult(result.FAILED);
				result.setMessage("保存失败");
				return result;
			}
		}
		return result;
	}
}
