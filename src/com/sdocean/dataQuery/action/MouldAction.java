package com.sdocean.dataQuery.action;

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
import com.sdocean.dataQuery.model.MouldModel;
import com.sdocean.dataQuery.service.MouldService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;

@Controller
public class MouldAction {

	private static Logger log = Logger.getLogger(MouldAction.class); 
	
	@Autowired
	MouldService mouldService;
	@Autowired
	OperationLogService logService;
	
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_consetting.do")
	public ModelAndView info_consetting(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/consetting_init");
	        return mav;  
	}
	
	/*
	 * 根据站点获得当前站点的水质评价模板
	 */
	@RequestMapping(value="getMouldByStationId.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getMouldByStationId(@ModelAttribute("model") MouldModel model,HttpServletRequest request,
			HttpServletResponse response){
		MouldModel result = mouldService.getMouldByStationId(model);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存新建或修改的水质评价模板
	 */
	@RequestMapping(value="saveMouldSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveMouldSetting(@ModelAttribute("model") MouldModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = mouldService.saveMouldSetting(model);
		//保存此次操作信息
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
}