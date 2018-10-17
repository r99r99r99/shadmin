package com.sdocean.station.action;

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
import com.sdocean.station.model.StationDeviceModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationDeviceService;

@Controller
public class StationDeviceAction {
	@Resource
	StationDeviceService stationDeviceService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_stationDevice.do")
	public ModelAndView info_station(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationDeviceInfo");
	        return mav;  
	}
	
	/*
	 * 展示站点设备配置列表
	 */
	@RequestMapping(value="showStationDeviceList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showStationDeviceList(@ModelAttribute("model") StationDeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得该用户的站点权限
		HttpSession session = request.getSession();
		List<StationModel> stations = (List<StationModel>) session.getAttribute("stations");
		
		PageResult page = new PageResult();
		List<UiColumn> cols = stationDeviceService.getCols4StationDeviceList();
		List<StationDeviceModel> rows = stationDeviceService.getStationDeviceList(model,stations);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 保存新增的站点设备配置
	 */
	@RequestMapping(value="saveNewStationDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewStationDevice(@ModelAttribute("model") StationDeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		//判断创建时间
		if(model!=null&&(model.getCreateTime()==null||model.getCreateTime().length()<1)){
			Date now = new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			model.setCreateTime(sdf.format(now));
		}
		Result result = stationDeviceService.saveNewStationDevice(model);
		//保存操作日志
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 保存修改的站点设备配置
	 */
	@RequestMapping(value="saveChangeStationDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeStationDevice(@ModelAttribute("model") StationDeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = stationDeviceService.saveChangeStationDevice(model);
		//保存操作日志
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 删除站点设备配置
	 */
	@RequestMapping(value="deleteStationDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleteStationDevice(@ModelAttribute("model") StationDeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = stationDeviceService.deleteStationDevice(model);
		//保存操作日志
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 根据站点ID，设备ID或的站点设备配置
	 */
	@RequestMapping(value="getStationDeviceByWidDid.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationDeviceByWidDid(@ModelAttribute("model") StationDeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		StationDeviceModel result = stationDeviceService.getStationDeviceByWidDid(model);
		return JsonUtil.toJson(result);
	}
}
