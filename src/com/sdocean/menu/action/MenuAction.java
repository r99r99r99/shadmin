package com.sdocean.menu.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sdocean.common.model.ZTreeModel;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.menu.service.SysMenuService;
import com.sdocean.role.model.RoleModel;

@Controller
public class MenuAction {
	@Resource
	SysMenuService menuService;
	
	
	/*
	 * 获得某角色下的菜单列表,以TREE的形式显示
	 */
	@RequestMapping(value="getMenus4Tree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getMenus4Tree(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> tree = menuService.getMenus4Tree(role);
		return JsonUtil.toJson(tree);
	}
	/*
	 * 获得某角色下的首页菜单列表,以TREE的形式显示
	 */
	@RequestMapping(value="getFirstMenus4Tree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getFirstMenus4Tree(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> tree = menuService.getFirstMenus4Tree(role);
		return JsonUtil.toJson(tree);
	}
}
