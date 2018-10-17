package com.sdocean.station.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.station.service.StationTypeService;

@Controller
public class StationTypeAction {
	@Resource
	StationTypeService typeService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_station_type.do")
	public ModelAndView info_station_type(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationTypeInfo");
	        return mav;  
	}
	
	@RequestMapping(value="showStationTypes.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showStationTypes(@ModelAttribute("type") StationTypeModel type,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果添加表头
		List<UiColumn> cols = typeService.getCols4TypeList();
		result.setCols(cols);
		//为查询结果添加结果
		List<StationTypeModel> rows = typeService.getTypeList(type);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存站点类型修改
	 */
	@RequestMapping(value="saveTypeChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveTypeChange(@ModelAttribute("model") StationTypeModel model,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = typeService.saveTypeChange(model);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	/*
	 * 新增站点类型
	 */
	@RequestMapping(value="saveNewType.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewType(@ModelAttribute("model") StationTypeModel model,HttpServletRequest request,
			HttpServletResponse response){
		//进行修改用户信息操作
		Result result = typeService.saveNewType(model);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	/*
	 * 查询站点类型结果
	 */
	@RequestMapping(value="getAllStationTypeList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAllStationTypeList(HttpServletRequest request,
			HttpServletResponse response){
		List<StationTypeModel> result = new ArrayList<StationTypeModel>();
		//初始化参数
		StationTypeModel type = new StationTypeModel();
		type.setIsactive(1);
		result = typeService.getTypeList(type);
		return JsonUtil.toJson(result);
	}
}
