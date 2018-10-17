package com.sdocean.main.action;

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
import com.sdocean.main.model.AiotMainConfigModel;
import com.sdocean.main.service.AiotMainConfigService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class AiotMainConfigAction {
	@Resource
	AiotMainConfigService aiotMainConfigService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	/*
	 * 为站点设备维护配置提供列表
	 */
	@RequestMapping(value="getAiotMainConfigList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAiotMainConfigList(@ModelAttribute("model") AiotMainConfigModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<AiotMainConfigModel> result = aiotMainConfigService.getAiotMainConfigList(model);
		return JsonUtil.toJson(result);
	}
	/*
	 * 例行维护配置跳转
	 */
	@RequestMapping("info_MainConfig.do")
	public ModelAndView info_MainConfig(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/main/mainConfigInfo");
	        return mav;  
	}
	/*
	 * 查询例行维护配置
	 */
	@RequestMapping(value="showAiotMainConfig.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showAiotMainConfig(@ModelAttribute("model") AiotMainConfigModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		List<UiColumn> cols = aiotMainConfigService.getCols4MainEditList();
		List<AiotMainConfigModel> rows = aiotMainConfigService.getAiotMainConfigList(model);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 新增例行维护配置
	 */
	@RequestMapping(value="saveNewMainConfig.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewMainConfig(@ModelAttribute("model") AiotMainConfigModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = aiotMainConfigService.saveNewMainConfig(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 修改例行维护配置
	 */
	@RequestMapping(value="saveChangeMainConfig.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeMainConfig(@ModelAttribute("model") AiotMainConfigModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = aiotMainConfigService.saveChangeMainConfig(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 删除例行维护配置
	 */
	@RequestMapping(value="deleMainConfig.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleMainConfig(@ModelAttribute("model") AiotMainConfigModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = aiotMainConfigService.deleMainConfig(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
}
