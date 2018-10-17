package com.sdocean.role.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.NgColumn;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.position.model.SysPosition;
import com.sdocean.position.service.SysPositionService;
import com.sdocean.role.model.RoleModel;
import com.sdocean.role.service.RoleService;
import com.sdocean.users.model.SysUser;

@Controller
public class RoleAction {

	private static Logger log = Logger.getLogger(RoleAction.class);  
	@Autowired
	private RoleService roleService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_sysRole.do")
	public ModelAndView handleRequestInternal(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/role/sysRoleInfo");
	        return mav;  
	}
	
	/*
	 * 获得角色的列表
	 */
	@RequestMapping(value="getRoleList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getRoleList(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//获得角色查询的表头
		List<UiColumn> cols = roleService.getCols4List();
		
		//获得角色查询的结果
		List<RoleModel> roles = roleService.getRoleList(role);
		
		result.setCols(cols);
		result.setRows(roles);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存角色-人员-菜单
	 */
	@RequestMapping(value="saveRoleUserMenu.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveRoleUserMenu(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = roleService.saveRoleUserMenu(role);
				
		logService.saveOperationLog(result,request);
				
		return JsonUtil.toJson(result.getMessage());
	}
	
	/*
	 * 保存角色-人员-站点
	 */
	@RequestMapping(value="saveRoleUserStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveRoleUserStation(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = roleService.saveRoleUserStation(role);
				
		logService.saveOperationLog(result,request);
				
		return JsonUtil.toJson(result.getMessage());
	}
	
	/*
	 * 新增角色
	 */
	@RequestMapping(value="saveAddRole.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveAddRole(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = roleService.saveAddRole(role);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	
	/*
	 * 编辑角色
	 */
	@RequestMapping(value="saveEditRole.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveEditRole(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = roleService.saveEditRole(role);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	
	/*
	 * S删除角色
	 */
	@RequestMapping(value="deleRole.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleRole(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = roleService.deleRole(role);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	/*
	 * 保存角色首页权限
	 */
	@RequestMapping(value="saveFirstMenuRole.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveFirstMenuRole(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = roleService.saveFirstMenuRole(role);
				
		logService.saveOperationLog(result,request);
				
		return JsonUtil.toJson(result.getMessage());
	}
}
