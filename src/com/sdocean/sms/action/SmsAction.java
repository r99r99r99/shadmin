package com.sdocean.sms.action;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.sms.model.SmsLog;
import com.sdocean.sms.model.SmsMouldModel;
import com.sdocean.sms.model.SmsSettingModel;
import com.sdocean.sms.service.SmsService;
import com.sdocean.users.model.SysUser;

@Controller
public class SmsAction {
	@Resource
	SmsService smsService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_smssetting.do")
	public ModelAndView info_smssetting(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/sms/smsSettingInfo");
	        return mav;  
	}
	
	@RequestMapping("info_smssetting4user.do")
	public ModelAndView info_smssetting4user(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/sms/smsSetting4userInfo");
		    CurrMenu menu = new CurrMenu();
		    menu.setCurl("info_smssetting4user.do");
		    HttpSession session = request.getSession();
		    session.setAttribute("currMenu", menu);
	        return mav;  
	}
	/*
	 * 跳转到短信发送记录页面
	 */
	@RequestMapping("info_smslog.do")
	public ModelAndView info_smslog(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/sms/smsLogInfo");
	        return mav;  
	}
	
	/*
	 * 展示短信配置中,某站点的人员列表
	 */
	@RequestMapping(value="showUsers4SmsSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showUsers4SmsSetting(@ModelAttribute("model") SmsSettingModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		//获得当前的人员信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		//得到展示表格的表头
		List<UiColumn> cols = smsService.getCols4TypeList();
		//得到展示表格的结果集
		List<SmsSettingModel> rows = smsService.getUsers4SmsSetting(model, user);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	
	/*
	 * 初始化当前用户的短信配置
	 */
	@RequestMapping(value="getSmsSetting4User.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getSmsSetting4User(HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		SysUser model = (SysUser) session.getAttribute("user");
		SmsSettingModel setting = new SmsSettingModel();
		setting = smsService.infoUserSetting(model);
		return JsonUtil.toJson(setting);
	}
	
	/*
	 * 初始化用户下的短信配置
	 */
	@RequestMapping(value="infoUserSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String infoUserSetting(@ModelAttribute("model") SysUser model,HttpServletRequest request,
			HttpServletResponse response){
		SmsSettingModel setting = new SmsSettingModel();
		setting = smsService.infoUserSetting(model);
		return JsonUtil.toJson(setting);
	}
	
	/*
	 * 保存用户短信配置
	 */
	@RequestMapping(value="saveUserSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveUserSetting(@ModelAttribute("model") SmsSettingModel model,HttpServletRequest request,
			HttpServletResponse response){
		
		Result result = new Result();
		result = smsService.saveUserSetting(model);
		
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 跳转到短信模板设置页面
	 */
	@RequestMapping("info_smsmould.do")
	public ModelAndView info_smsmould(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/sms/smsMouldInfo");
	        return mav;  
	}
	/*
	 * 展示模板列表
	 */
	@RequestMapping(value="showSmsMouldList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showSmsMouldList(@ModelAttribute("model") SmsMouldModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		//得到展示表格的表头
		List<UiColumn> cols = smsService.getCols4MouldList();
		//得到展示表格的结果集
		List<SmsMouldModel> rows = smsService.getSmsMoulds4Rows(model);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	
	/*
	 * 保存短信模板修改
	 */
	@RequestMapping(value="saveSmsMouldChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveSmsMouldChange(@ModelAttribute("model") SmsMouldModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = smsService.saveSmsMouldChange(model);
		
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 保存短信模板新增
	 */
	@RequestMapping(value="saveNewSmsMould.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewSmsMould(@ModelAttribute("model") SmsMouldModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = smsService.saveNewSmsMould(model);
		
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	@RequestMapping(value="saveSmsSetting4User.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveSmsSetting4User(@ModelAttribute("model") SmsSettingModel model,HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setUserId(user.getId());
		Result result = new Result();
		result = smsService.saveUserSetting(model);
		
		return result.getMessage();
	}
	/*
	 * 得到短信发送记录列表
	 */
	@RequestMapping(value="getSmsLogs.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getSmsLogs(@ModelAttribute("model") SmsLog model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		List<UiColumn> cols = smsService.getCols4LogList();
		List<SmsLog> rows = smsService.getSmsLogs(model);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 跳转到短信发送配置
	 */
	@RequestMapping("info_smsstation.do")
	public ModelAndView info_smsstation(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/sms/smsStationInfo");
	        return mav;  
	}
	/*
	 * 保存短信发送配置
	 */
	@RequestMapping(value="saveSmsStationSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveSmsStationSetting(@ModelAttribute("model") SmsSettingModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = smsService.saveSmsStationSetting(model);
		logService.saveOperationLog(result, request);
		return JsonUtil.toJson(result);
	}
	
}
