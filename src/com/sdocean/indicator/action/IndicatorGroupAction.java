package com.sdocean.indicator.action;

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
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorGroupModel;
import com.sdocean.indicator.service.IndicatorGroupService;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class IndicatorGroupAction {

	private static Logger log = Logger.getLogger(IndicatorGroupAction.class);  
	@Autowired
	private IndicatorGroupService indicatorGroupService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	
	@RequestMapping("info_indicator_group.do")
	public ModelAndView info_indicator_group(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/indicator/indicatorGroupInfo");
	        return mav;  
	}
	/*
	 * 为参数组管理查询结果
	 */
	@RequestMapping(value="showIndicatorGroups.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showIndicatorGroups(@ModelAttribute("model") IndicatorGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = indicatorGroupService.getCols4List();
		result.setCols(cols);
		
		List<IndicatorGroupModel> rows = indicatorGroupService.showIndicatorGroups(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 获得符合条件的参数组的集合
	 */
	@RequestMapping(value="getIndicatorGroups.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorGroups(@ModelAttribute("model") IndicatorGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<IndicatorGroupModel> rows = indicatorGroupService.showIndicatorGroups(model);
		return JsonUtil.toJson(rows);
	}
	/*
	 * 保存修改的公共代码
	 */
	@RequestMapping(value="saveIndicaotrGroupChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveIndicaotrGroupChange(@ModelAttribute("model") IndicatorGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = indicatorGroupService.saveGroupChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 保存新增的公共代码
	 */
	@RequestMapping(value="saveNewIndicatorGroup.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewIndicatorGroup(@ModelAttribute("model") IndicatorGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = indicatorGroupService.saveNewGroup(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 删除选中的编码
	 */
	@RequestMapping(value="deleIndicatorGroups.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleIndicatorGroups(@ModelAttribute("model") IndicatorGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = indicatorGroupService.deleGroup(model.getIds());
		logService.saveOperationLog(result,request);
		return result.getMessage();
	}
	
}
