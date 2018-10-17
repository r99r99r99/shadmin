package com.sdocean.dictionary.action;

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
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.dictionary.service.PublicService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class PublicAction {

	private static Logger log = Logger.getLogger(PublicAction.class);  
	@Autowired
	private PublicService publicService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	
	@RequestMapping("info_public.do")
	public ModelAndView info_public(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dictionary/publicInfo");
	        return mav;  
	}
	
	/*
	 * 通过parentCode查询到公共代码列表
	 */
	@RequestMapping(value="getPublicList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getPublicList(@ModelAttribute("model") PublicModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<PublicModel> publics = publicService.getPublicsByParent(model.getParentCode());
		return JsonUtil.toJson(publics);
	}
	
	/*
	 * 为公共代码管理查询结果
	 */
	@RequestMapping(value="showPublics.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showPublics(@ModelAttribute("model") PublicModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = publicService.getCols4List();
		result.setCols(cols);
		
		List<PublicModel> rows = publicService.getPublics(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 保存修改的公共代码
	 */
	@RequestMapping(value="savePublicChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String savePublicChange(@ModelAttribute("model") PublicModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = publicService.savePublicChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 保存新增的公共代码
	 */
	@RequestMapping(value="saveNewPublic.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewPublic(@ModelAttribute("model") PublicModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = publicService.saveNewPublic(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 删除选中的编码
	 */
	@RequestMapping(value="delePublics.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String delePublics(@ModelAttribute("model") PublicModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = publicService.delePublic(model.getIds());
		logService.saveOperationLog(result,request);
		return result.getMessage();
	}
	
}
