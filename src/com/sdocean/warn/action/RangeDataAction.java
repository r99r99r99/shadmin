package com.sdocean.warn.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.dictionary.service.PublicService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;
import com.sdocean.warn.model.RangeDataModel;
import com.sdocean.warn.model.WarnModel;
import com.sdocean.warn.model.WarnValueModel;
import com.sdocean.warn.service.RangeDataService;
import com.sdocean.warn.service.WarnService;

@Controller
public class RangeDataAction {
	@Resource
	RangeDataService rangeDataService;
	@Resource
	OperationLogService logService;
	@Resource
	StationService stationService;
	@Resource
	PublicService publicService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_rangevalue.do")
	public ModelAndView info_errorvalue(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/warn/rangeDataInfo");
	        return mav;  
	}
	
	@RequestMapping(value="showRangeData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showRangeData(@ModelAttribute("model") RangeDataModel model,HttpServletRequest request,
			HttpServletResponse response){
		
		PageResult result = new PageResult();
		//为查询结果添加表头
		List<UiColumn> cols = rangeDataService.getCols4RangeDataList();
		result.setCols(cols);
		//为查询结果添加结果
		List<RangeDataModel> rows = rangeDataService.getRangeDataList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	@RequestMapping(value="updateRangeData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateRangeData(@ModelAttribute("model") RangeDataModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = rangeDataService.updateRangeData(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	@RequestMapping(value="saveNewRangeData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewRangeData(@ModelAttribute("model") RangeDataModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = rangeDataService.saveNewRangeData(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	@RequestMapping(value="deleRangeData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleRangeData(@ModelAttribute("model") RangeDataModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = rangeDataService.deleRangeData(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
}
