package com.sdocean.station.action;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationDeviceIndicator;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationDeviceIndicatorService;
import com.sdocean.users.model.SysUser;

@Controller
public class StationDeviceIndicatorAction {
	@Resource
	StationDeviceIndicatorService sdiService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_stationIndicator.do")
	public ModelAndView info_station(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationIndicator");
	        return mav;  
	}
	
	@RequestMapping(value="getDeiviceIndicatorsByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDeiviceIndicatorsByStation(@ModelAttribute("model") StationDeviceIndicator model,HttpServletRequest request,
			HttpServletResponse response){
	
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		list = sdiService.getDeiviceIndicatorsByStation(model.getStationId());
		return JsonUtil.toJson(list);
	}
	/*
	 * 获得用户权限下的站点的列表
	 */
	@RequestMapping(value="getStation4DeviceIndicator.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStation4DeviceIndicator(@ModelAttribute("model") StationDeviceIndicator model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		List<UiColumn> cols = sdiService.getCols4Stations();
		List<StationModel> stations = new ArrayList<StationModel>();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		stations = sdiService.getStation4DeviceIndicator(user.getId(), model.getStationName());
		page.setCols(cols);
		page.setRows(stations);
		return JsonUtil.toJson(page);
	}
	/*
	 * 保存站点设备参数
	 */
	@RequestMapping(value="saveStationDeviceIndicator.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveStationDeviceIndicator(@ModelAttribute("model") StationDeviceIndicator model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = sdiService.saveStationDeviceIndicator(model);
		
		logService.saveOperationLog(result, request);
		return JsonUtil.toJson(result.getMessage());
	}
}
