package com.sdocean.dictionary.action;

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
import com.sdocean.dictionary.model.WaterQualityStandardModel;
import com.sdocean.dictionary.service.WaterQualityStandardService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class WaterQualityStandardAction {
	@Resource
	WaterQualityStandardService standardService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_water_level.do")
	public ModelAndView info_water_level(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dictionary/waterLevelInfo");
	        return mav;  
	}
	
	@RequestMapping(value="getStandardList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStandardList(@ModelAttribute("model") WaterQualityStandardModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = standardService.getCols4GroupList();
		result.setCols(cols);
		//为查询结果增加列表
		List<WaterQualityStandardModel> rows = standardService.getStandardList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	
	/*
	 * 保存修改信息
	 */
	@RequestMapping(value="saveQualityChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveQualityChange(@ModelAttribute("model") WaterQualityStandardModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = standardService.saveQualityChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新增信息
	 */
	@RequestMapping(value="saveNewQuality.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewQuality(@ModelAttribute("model") WaterQualityStandardModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = standardService.saveNewQuality(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 删除选中的水质等级
	 */
	@RequestMapping(value="deleWaterQulity.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleWaterQulity(@ModelAttribute("model") WaterQualityStandardModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = standardService.deleWaterQulity(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
}
