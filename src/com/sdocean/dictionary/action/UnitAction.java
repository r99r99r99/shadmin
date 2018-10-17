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
import com.sdocean.common.model.SelectTree;
import com.sdocean.dictionary.model.UnitModel;
import com.sdocean.dictionary.service.UnitService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class UnitAction {
	@Resource
	UnitService unitService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_unit.do")
	public ModelAndView info_unit(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dictionary/unitInfo");
	        return mav;  
	}
	
	@RequestMapping(value="showUnitList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showUnitList(@ModelAttribute("model") UnitModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = unitService.getCols4GroupList();
		result.setCols(cols);
		//为查询结果增加列表
		List<UnitModel> rows = unitService.getUnitList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 获得单位列表
	 */
	@RequestMapping(value="getUnitList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUnitList(@ModelAttribute("model") UnitModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<UnitModel> rows = unitService.getUnitList(model);
		return JsonUtil.toJson(rows);
	}
	
	/*
	 * 保存修改信息
	 */
	@RequestMapping(value="saveUnitChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveUnitChange(@ModelAttribute("model") UnitModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = unitService.saveUnitChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新增信息
	 */
	@RequestMapping(value="saveNewUnit.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewUnit(@ModelAttribute("model") UnitModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = unitService.saveNewUnit(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 以树的形式展示单位列表
	 */
	@RequestMapping(value="getUnitList4Tree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUnitList4Tree(@ModelAttribute("model") UnitModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = unitService.getUnitList4Tree(model);
		return JsonUtil.toJson(list);
	}
	
}
