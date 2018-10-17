package com.sdocean.main.action;

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
import com.sdocean.main.model.AiotMainConfigModel;
import com.sdocean.main.model.StationDeviceMainModel;
import com.sdocean.main.service.StationDeviceMainService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Controller
public class StationDeviceMainAction {
	@Resource
	StationDeviceMainService stationDeviceMainService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_stationDeviceMain.do")
	public ModelAndView info_stationDeviceMain(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/main/stationDeviceMainInfo");
	        return mav;  
	}
	/*
	 * 为站点设备维护配置提供查询列表
	 */
	@RequestMapping(value="showStationDeviceMainList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showStationDeviceMainList(@ModelAttribute("model") StationDeviceMainModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//获得SESSION中保存的权限站点列表
		HttpSession session = request.getSession();
		List<StationModel> stations = (List<StationModel>) session.getAttribute("stations");
		List<UiColumn> cols = stationDeviceMainService.getCols4MainList();
		List<StationDeviceMainModel> rows = stationDeviceMainService.getStationDeviceMainList(model, stations);
		result.setCols(cols);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 新增站点维护配置
	 */
	@RequestMapping(value="saveStationDeviceMain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveStationDeviceMain(@ModelAttribute("model") StationDeviceMainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = stationDeviceMainService.saveStationDeviceMain(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 修改站点维护配置
	 */
	@RequestMapping(value="saveChangeStationDeviceMain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeStationDeviceMain(@ModelAttribute("model") StationDeviceMainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = stationDeviceMainService.saveChangeStationDeviceMain(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 删除站点维护配置
	 */
	@RequestMapping(value="deleStationDeviceMain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleStationDeviceMain(@ModelAttribute("model") StationDeviceMainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = stationDeviceMainService.deleStationDeviceMain(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 根据站点和设备获得有效的类型维护类型列表
	 */
	@RequestMapping(value="getAiotMainConfigListByStationDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAiotMainConfigListByStationDevice(@ModelAttribute("model") StationDeviceMainModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<AiotMainConfigModel> list = stationDeviceMainService.getAiotMainConfigListByStationDevice(model);
		return JsonUtil.toJson(list);
	}
}
