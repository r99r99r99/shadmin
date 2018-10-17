package com.sdocean.warn.action;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.warn.model.DeviceAlarmConfig;
import com.sdocean.warn.model.DeviceAlarmModel;
import com.sdocean.warn.service.DeviceAlarmService;

@Controller
public class DeviceAlarmAction {
	@Resource
	DeviceAlarmService deviceAlarmService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_devicealarm.do")
	public ModelAndView info_station(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/warn/deviceAlarmInfo");
	        return mav;  
	}
	
	/*
	 * 展示站点设备配置列表
	 */
	@RequestMapping(value="showDeviceAlarmList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showDeviceAlarmList(@ModelAttribute("model") DeviceAlarmModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得该用户的站点权限
		HttpSession session = request.getSession();
		List<StationModel> stations = (List<StationModel>) session.getAttribute("stations");
		
		PageResult page = new PageResult();
		List<UiColumn> cols = deviceAlarmService.getCols4DeviceAlarmList();
		List<DeviceAlarmModel> rows = deviceAlarmService.getDeviceAlarmList(model,stations);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 保存新增的站点设备配置
	 */
	@RequestMapping(value="saveNewDeviceAlarm.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewDeviceAlarm(@ModelAttribute("model") DeviceAlarmModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = deviceAlarmService.saveNewDeviceAlarm(model);
		//保存操作日志
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 保存修改的站点设备配置
	 */
	@RequestMapping(value="saveChangeDeviceAlaram.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeDeviceAlaram(@ModelAttribute("model") DeviceAlarmModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = deviceAlarmService.saveChangeDeviceAlaram(model);
		//保存操作日志
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 删除站点设备配置
	 */
	@RequestMapping(value="deleteDeviceAlarm.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleteDeviceAlarm(@ModelAttribute("model") DeviceAlarmModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = deviceAlarmService.deleteDeviceAlarm(model);
		//保存操作日志
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	@RequestMapping(value="getDeviceAlarmConfigList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDeviceAlarmConfigList(@ModelAttribute("model") DeviceAlarmConfig model,HttpServletRequest request,
			HttpServletResponse response){
		List<DeviceAlarmConfig> list  = deviceAlarmService.getDeviceAlarmConfigList(model);
		return JsonUtil.toJson(list);
	}
}
