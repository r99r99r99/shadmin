package com.sdocean.domain.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.domain.model.DomainForm;
import com.sdocean.domain.model.DomainLevel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.domain.model.DomainResult;
import com.sdocean.domain.model.DomainThreshold;
import com.sdocean.domain.server.DomainService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Controller
public class DomainAction {

	private static Logger log = Logger.getLogger(DomainAction.class);  
	@Autowired
	private DomainService domainService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	
	@RequestMapping("info_domain.do")
	public ModelAndView info_domain(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/domain/domainInfo");
	        return mav;  
	}
	
	/*
	 * 查看功能区列表
	 */
	@RequestMapping(value="showDomainList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showDomainList(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = domainService.getCols4DomainList();
		result.setCols(cols);
		
		List<DomainModel> rows = domainService.getDomainList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 *得到功能区列表 
	 */
	@RequestMapping(value="getDomainList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDomainList(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		
		List<DomainModel> rows = domainService.getDomainList(model);
		return JsonUtil.toJson(rows);
	}
	
	
	/*
	 * 保存新增的功能区
	 */
	@RequestMapping(value="saveNewDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewDomain(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.saveNewDomain(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新增的功能区
	 */
	@RequestMapping(value="saveChangeDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeDomain(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.saveChangeDomain(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	

	/*
	 * 停用 功能区
	 */
	@RequestMapping(value="deleDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleDomain(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.deleDomain(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存功能区配置
	 */
	@RequestMapping(value="saveDomainStationIndicator.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveDomainStationIndicator(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.saveDomainStationIndicator(model);
		logService.saveOperationLog(result,request);
		
		return JsonUtil.toJson(result.getMessage());
	}
	
	/*
	 * 获得首页的站点功能区展示
	 */
	
	@RequestMapping(value="getDomainResultsByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDomainResultsByStation(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<DomainResult> results = domainService.getDomainResultsByStation(model);
		
		return JsonUtil.toJson(results);
	}
	
	/*
	 * 通过功能区查询功能区的层级
	 */
	@RequestMapping(value="showLevelListByDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showLevelListByDomain(@ModelAttribute("model") DomainLevel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = domainService.getCols4DomainLevelList();
		result.setCols(cols);
		
		List<DomainLevel> rows = domainService.getLevelListByDomain(model);
		result.setRows(rows);
		
		
		return JsonUtil.toJson(result);
	}
	
	
	/*
	 * 保存新的功能区层级
	 */
	@RequestMapping(value="saveNewDomainLevel.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewDomainLevel(@ModelAttribute("model") DomainLevel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.saveNewDomainLevel(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新的功能区层级
	 */
	@RequestMapping(value="updateDomainLevel.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateDomainLevel(@ModelAttribute("model") DomainLevel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.updateDomainLevel(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新的功能区层级
	 */
	@RequestMapping(value="deleDomainLevel.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleDomainLevel(@ModelAttribute("model") DomainLevel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.deleDomainLevel(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 跳转到功能区阈值设置页面
	 */
	@RequestMapping("info_domainThreshold.do")
	public ModelAndView info_domainThreshold(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/domain/domainThresholdInfo");
	        return mav;  
	}
	
	@RequestMapping(value="getThresholdListByDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getThresholdListByDomain(@ModelAttribute("model") DomainThreshold model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = domainService.getThresholdCols4Domain(model);
		result.setCols(cols);
		
		List<Map<String, Object>> rows = domainService.getThresholdRows4Domain(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 查询功能区内某参数的阈值设置
	 */
	@RequestMapping(value="showThresholdListByDomainIndicator.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showThresholdListByDomainIndicator(@ModelAttribute("model") DomainThreshold model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = domainService.getThresholdColsByDomainIndicator();
		result.setCols(cols);
		
		List<DomainThreshold> rows = domainService.getThresholdListByDomainIndicator(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 删除功能区阈值设置
	 */
	@RequestMapping(value="deleDomainThreshold.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleDomainThreshold(@ModelAttribute("model") DomainThreshold model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.deleDomainThreshold(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存功能区阈值
	 */
	@RequestMapping(value="saveDomainThreshold.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveDomainThreshold(
			@ModelAttribute("model") DomainForm model,
			HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = domainService.saveDomainThreshold(model);
		logService.saveOperationLog(result,request);
		return result.getMessage();
	}
}