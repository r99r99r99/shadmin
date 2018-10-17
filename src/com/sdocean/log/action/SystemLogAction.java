package com.sdocean.log.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.model.OperationLogModel;
import com.sdocean.log.model.SysLog;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.log.service.SysLoginLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;

@Controller
public class SystemLogAction {

	private static Logger log = Logger.getLogger(SystemLogAction.class);  
	@Resource
	SysLoginLogService loginLogService;
	@Resource
	OperationLogService logService;
	@Resource
	StationService stationService;
	@Autowired
	private ConfigInfo info;
	
	/*
	 * 跳转到登录日志查询页面
	 */
	@RequestMapping("info_sysLogin.do")
	public ModelAndView info_sysLogin(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/system/sysLoginInfo");
	        return mav;  
	}
	/*
	 * 跳转到操作记录查询页面
	 */
	@RequestMapping("info_sysOperation.do")
	public ModelAndView info_sysOperation(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/system/sysOperationInfo");
	        return mav;  
	}
	/*
	 * 跳转到登录日志查询页面
	 */
	@RequestMapping("info_sysLog.do")
	public ModelAndView info_sysLog(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/system/sysLogInfo");
	        return mav;  
	}
	/*
	 * 为登录日志查询初始化查询条件
	 */
	@RequestMapping(value="init_sysLogin.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String init_sysLogin(HttpServletRequest request,
			HttpServletResponse response){
		SysLoginLogModel model = new SysLoginLogModel();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//默认开始时间为一个月以前
		calendar.add(Calendar.MONTH, -1);
		// 添加默认开始时间
		String time1 = df.format(calendar.getTime()) ;
		// 添加默认结束时间
		String time2 = df.format(new Date());
		model.setBeginTime(time1);
		model.setEndTime(time2);
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 初始化设备状态查询页面
	 */
	@RequestMapping(value="sysLogShowInit.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String sysLogShowInit(HttpServletRequest request,
			HttpServletResponse response){
		SysLog model = new SysLog();
		
		//获得当前登录的用户
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		//初始化该用户权限下的站点列表
		List<StationModel> stations = stationService.getStations4User(user);
	    //初始化上报时间
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    String endDate = beginDf.format(calendar.getTime());
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    model.setStations(stations);
	    model.setBeginTime(beginDate);
	    model.setEndTime(endDate);
		return JsonUtil.toJson(model);
	}
	/*
	 * 为参数组管理查询结果
	 */
	@RequestMapping(value="showLoginLogs.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showLoginLogs(@ModelAttribute("model") SysLoginLogModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = loginLogService.getLoginCols4List();
		result.setCols(cols);
		
		List<SysLoginLogModel> rows = loginLogService.getLoginLogList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 为操作日志查询初始化查询条件
	 */
	@RequestMapping(value="init_sysOperation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String init_sysOperation(HttpServletRequest request,
			HttpServletResponse response){
		OperationLogModel model = new OperationLogModel();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//默认开始时间为一个月以前
		calendar.add(Calendar.MONTH, -1);
		// 添加默认开始时间
		String time1 = df.format(calendar.getTime()) ;
		// 添加默认结束时间
		String time2 = df.format(new Date());
		model.setBeginTime(time1);
		model.setEndTime(time2);
		return JsonUtil.toJson(model);
	}
	/*
	 * 为操作日志查询结果
	 */
	@RequestMapping(value="showOperationLogs.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showOperationLogs(@ModelAttribute("model") OperationLogModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = logService.getLoginCols4List();
		result.setCols(cols);
		
		List<OperationLogModel> rows = logService.getOperationLogs(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 为系统状态日志查询提供结果
	 */
	@RequestMapping(value="showSysLogs.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showSysLogs(@ModelAttribute("model") SysLog model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		List<UiColumn> cols = logService.getCols4SysLogs(model);
		List<Map<String, Object>> rows = logService.getRows4SysLogs(model);
		result.setCols(cols);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
}
