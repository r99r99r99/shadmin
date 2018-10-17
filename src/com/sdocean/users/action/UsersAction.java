package com.sdocean.users.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.file.action.FileUpload;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.page.model.NgColumn;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.role.model.RoleModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.users.service.UsersManager;

@Controller
public class UsersAction {
	@Resource
	UsersManager usersManager;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_sysUser.do")
	public ModelAndView handleRequestInternal(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/user/sysUserInfo");
	        return mav;  
	}
	
	@RequestMapping(value="showUserList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showUserList(@ModelAttribute("model") SysUser model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果添加表头
		List<UiColumn> cols = usersManager.getCols4UserList();
		result.setCols(cols);
		//为查询结果添加结果
		List<SysUser> rows = usersManager.getRows4UserList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	
	//保存用户编辑信息
	@RequestMapping(value="saveUserChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveUserChange(@ModelAttribute("user") SysUser user,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = usersManager.saveUserChange(user);
		
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	//保存用户新建信息
	@RequestMapping(value="saveNewUser.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewUser(@ModelAttribute("user") SysUser user,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = usersManager.saveNewUser(user);
		
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 获得所有有效的公司下的所有有效人员的列表,展示为ZTREE形式
	 */
	@RequestMapping(value="getUsers4ZTree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUsers4ZTree(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> trees = usersManager.getCompUsers4ZTree(role);
		return JsonUtil.toJson(trees);
	}
	/*
	 * 用户注销操作
	 * 清除SESSION信息
	 */
	@RequestMapping("logout.do")
	public ModelAndView logOut(HttpServletRequest request,
			HttpServletResponse response) throws Exception
	{
		try {
			HttpSession session = request.getSession();
			// 清空session信息
			session.removeAttribute("userModel");
			session.removeAttribute("userId");
			session.removeAttribute("errorLoginTimes");
			// 销毁所有session
			//session.invalidate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String error = "登出成功";
		//return new ModelAndView("redirect:indexlogin.do");
		return new ModelAndView("/"+info.getPageVision()+"/login","error",error);  
	}
	
	/*
	 * 用户编辑个人信息
	 */
	@RequestMapping("userSettingInfo.do")
	public ModelAndView userSettingInfo(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/user/userSettingInfo");
		    CurrMenu menu = new CurrMenu();
		    menu.setCurl("userSettingInfo.do");
		    HttpSession session = request.getSession();
		    session.setAttribute("currMenu", menu);
	        return mav;  
	}
	
	/*
	 * 获得当前登录人员的人员信息
	 */
	@RequestMapping(value="getUserNow.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUserNow(HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		//重新从数据库读取用户信息
		SysUser sysUser = usersManager.getUsersByAccount(user.getUserName());
		return JsonUtil.toJson(sysUser);
	}
	
	/*
	 * 修改用户的信息
	 */
	@RequestMapping(value="saveUserNowChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveUserNowChange(@RequestParam(required=false) MultipartFile file,
			@RequestParam(value = "realName", required = false) String realName,
			@RequestParam(value = "telephone", required = false) String telephone,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "email", required = false) String email,
			//@ModelAttribute("user") SysUser model,
			HttpServletRequest request,
			HttpServletResponse response){
		SysUser model = new SysUser();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setId(user.getId());
		model.setRealName(realName);
		model.setTelephone(telephone);
		model.setPhone(phone);
		model.setBirthday(birthday);
		model.setEmail(email);
		String pic = "";
		//如果有上传图片的话,保存图片
		if(file!=null){
			FileUpload fileupload = new FileUpload();
			String realpath;
			try {
				realpath = fileupload.saveFileUpload(info,"images/user", file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "保存失败";
			}
			pic="images/user"+"/"+realpath;
		}
		if(pic.length()<1){
			pic="liaohe/resources/assets/avatars/user.jpg";
		}
		model.setPicurl(pic);
		//获得当前人员的人员信息
		Result result = null;
		result = usersManager.saveUserNowChange(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	/*
	 * 用户修改密码
	 */
	@RequestMapping("passSettingInfo.do")
	public ModelAndView passSettingInfo(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/user/passSettingInfo");
		    CurrMenu menu = new CurrMenu();
		    menu.setCurl("passSettingInfo.do");
		    HttpSession session = request.getSession();
		    session.setAttribute("currMenu", menu);
	        return mav;  
	}
	
	/*
	 * 用户保存新密码
	 */
	@RequestMapping(value="saveUserPassword.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveUserPassword(@ModelAttribute("model") SysUser model,HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setId(user.getId());
		Result result = null;
		result = usersManager.saveUserPassword(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	/*
	 * 得到站点下的用户列表
	 */
	@RequestMapping(value="getUsersByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUsersByStation(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SysUser> users = new ArrayList<SysUser>();
		users = usersManager.getUsersByStation(model);
		return JsonUtil.toJson(users);
	}
	/*
	 * 为短信发送配置提供某站点下人员的列表，并标记已经有短信接收的权限的用户
	 */
	@RequestMapping(value="getUsers4StationSms.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUsers4StationSms(@ModelAttribute("model") SmsSettingModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> trees = usersManager.getCompUsers4StationSmsZTree(model);
		return JsonUtil.toJson(trees);
	}
}
