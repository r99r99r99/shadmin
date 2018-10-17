package com.sdocean.device.action;

import java.util.ArrayList;
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
import com.sdocean.common.model.SelectTree;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.device.service.DeviceService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Controller
public class DeviceAction {

	private static Logger log = Logger.getLogger(DeviceAction.class);  
	@Autowired
	private DeviceService deviceService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	
	@RequestMapping("info_device.do")
	public ModelAndView info_public(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/device/deviceInfo");
	        return mav;  
	}
	
	/*
	 * 为设备管理查询结果
	 */
	@RequestMapping(value="showDevices.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showDevices(@ModelAttribute("model") DeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = deviceService.getCols4List();
		result.setCols(cols);
		
		List<DeviceModel> rows = deviceService.getDevices(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 保存修改的公共代码
	 */
	@RequestMapping(value="saveDeviceChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveDeviceChange(@ModelAttribute("model") DeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = deviceService.saveDeviceChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 保存新增的公共代码
	 */
	@RequestMapping(value="saveNewDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewDevice(@ModelAttribute("model") DeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = deviceService.saveNewDevice(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 保存新增的公共代码
	 */
	@RequestMapping(value="getDeviceListByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDeviceListByStation(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		list = deviceService.getDeviceListByStation(model);
		return JsonUtil.toJson(list);
	}
	/*
	 * 获得当前站点下的所有的设备的列表
	 */
	@RequestMapping(value="getDevices4Station.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDevices4Station(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		list = deviceService.getDevices4Station(model);
		return JsonUtil.toJson(list);
	}
	/*
	 * 获得当前站点下有展示权限的设备的列表
	 */
	@RequestMapping(value="getDevices4Station4Show.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDevices4Station4Show(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		
		list = deviceService.getDevices4Station4Show(model);
		return JsonUtil.toJson(list);
	}
	
	/*
	 * 查询所有的设备列表
	 */
	@RequestMapping(value="getDevices.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDevices(HttpServletRequest request,
			HttpServletResponse response){
		DeviceModel model = new DeviceModel();
		List<DeviceModel> rows = deviceService.getDevices(model);
		return JsonUtil.toJson(rows);
	}
	
	/*
	 * 获得当前站点下有展示权限的系统设备的列表
	 */
	@RequestMapping(value="getDevices4Station4System.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDevices4Station4System(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<DeviceModel> list = new ArrayList<DeviceModel>();
		
		list = deviceService.getDevices4Station4System(model);
		return JsonUtil.toJson(list);
	}
}
