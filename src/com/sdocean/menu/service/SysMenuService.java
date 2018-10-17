package com.sdocean.menu.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.ZTreeModel;
import com.sdocean.menu.dao.SysMenuDao;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.role.model.RoleModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class SysMenuService {
	
	@Autowired
	SysMenuDao menuDao;
	
	//获取该用户权限内的所有的后台父菜单的列表
	public List<SysMenu> getMenuListByUser(SysUser user){
		List<SysMenu> menuList = new ArrayList<SysMenu>();
		
		menuList = menuDao.getMenuListByUser(user);
		//遍历所有父菜单列表,得到所有的有权限的有效的子菜单
		for(SysMenu menu:menuList){
			menu = menuDao.getMenuByPmenu(menu,user);
		}
		return menuList;
	}
	/*
	 * 通过子类ID获得menu信息
	 */
	public CurrMenu getCurrMenuById(String menuId){
		return menuDao.getCurrMenuById(menuId);
	}
	
	/*
	 * 查询当前角色的菜单列表,并通过TREE的形式展现
	 */
	public List<ZTreeModel> getMenus4Tree(RoleModel role){
		return menuDao.getMenus4Tree(role);
	}
	
	 /*
	  * 查询当前菜单的首页菜单权限 ,并通过TREE的形式展示
	  */
	public List<ZTreeModel> getFirstMenus4Tree(RoleModel role){
		return menuDao.getFirstMenus4Tree(role);
	}
	/*
	 * 查询当前用户的首页菜单权限
	 */
	public SysMenu getFirstMenu(SysUser user){
		return menuDao.getFirstMenu(user);
	}
	/*
	 * 将当前用户的菜单保存到SESSION中
	 */
	public void saveCurrMenu(CurrMenu menu,HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("currMenu", menu);
    	@SuppressWarnings("unchecked")
		List<SysMenu> menuList = (List<SysMenu>) session.getAttribute("menuList");
    	for(SysMenu m:menuList){
    		m.setIsopen(0);
    		if(m.getCode().equals(menu.getpMenuId())){
    			m.setIsopen(1);
    			for(SysMenu c:m.getChildMenu()){
    				c.setIscurr(0);
    				if(c.getCode().equals(menu.getcMenuId())){
    					c.setIscurr(1);
    				}
    			}
    		}
    	}
    	session.setAttribute("menuList", menuList);
	}
}
