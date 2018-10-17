package com.sdocean.role.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.page.model.UiColumn;
import com.sdocean.role.dao.RoleDao;
import com.sdocean.role.model.RoleModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	/*
	 * 获得条件内的角色列表
	 */
	public List<RoleModel> getRoleList(RoleModel role){
		
		return roleDao.getRoleList(role);
	}
	
	/*
	 * 获得角色查询的表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("角色名称", "name", true, "*");
		UiColumn col3 = new UiColumn("状态", "isactiveName", true, "*");
		UiColumn col4 = new UiColumn("备注", "remark", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		
		return cols;
	}

	/*
	 * 保存角色-人员-菜单
	 * 
	 */
	public Result saveRoleUserMenu(RoleModel role){
		return roleDao.saveRoleUserMenu(role);
	}
	/*
	 * 保存角色-人员-菜单
	 * 
	 */
	public Result saveRoleUserStation(RoleModel role){
		return roleDao.saveRoleUserStation(role);
	}
	
	/*
	 * 保存新建角色
	 */
	public Result saveAddRole(RoleModel role){
		return roleDao.saveAddRole(role);
	}
	
	/*
	 * 保存编辑角色
	 */
	public Result saveEditRole(RoleModel role){
		return roleDao.saveEditRole(role);
	}
	
	/*
	 * 删除选中的角色
	 */
	public Result deleRole(RoleModel role){
		return roleDao.deleRole(role);
	}
	/*
	 * 保存首页角色权限
	 */
	public Result saveFirstMenuRole(RoleModel role){
		return roleDao.saveFirstMenuRole(role);
	}
}
