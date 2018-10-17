package com.sdocean.dictionary.action;

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
import com.sdocean.dictionary.model.UnitGroupModel;
import com.sdocean.dictionary.service.UnitGroupService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class UnitGroupAction {
	@Resource
	UnitGroupService unitGroupService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_unit_group.do")
	public ModelAndView info_unit_group(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dictionary/unitGroup");
	        return mav;  
	}
	
	@RequestMapping(value="showGroupList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showGroupList(@ModelAttribute("model") UnitGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = unitGroupService.getCols4GroupList();
		result.setCols(cols);
		//为查询结果增加列表
		List<UnitGroupModel> rows = unitGroupService.getUnitGroups(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存修改信息
	 */
	@RequestMapping(value="saveGroupChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveGroupChange(@ModelAttribute("model") UnitGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = unitGroupService.saveGroupChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新增信息
	 */
	@RequestMapping(value="saveNewGroup.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewGroup(@ModelAttribute("model") UnitGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = unitGroupService.saveNewGroup(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 获得有效的单位组的集合
	 */
	@RequestMapping(value="getUnitGroups.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getUnitGroups(HttpServletRequest request,
			HttpServletResponse response){
		List<UnitGroupModel> list = new ArrayList<UnitGroupModel>();
		list = unitGroupService.getUnitGroups();
		
		
		return JsonUtil.toJson(list);
	}
	
}
