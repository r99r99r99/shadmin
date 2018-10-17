package com.sdocean.company.action;

import java.util.ArrayList;
import java.util.List;

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
import com.sdocean.common.model.SelectTree;
import com.sdocean.company.model.CompanyModel;
import com.sdocean.company.model.SysCompanyModel;
import com.sdocean.company.service.CompanyService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Controller
public class CompanyAction {

	private static Logger log = Logger.getLogger(CompanyAction.class);  
	@Autowired
	private CompanyService companyService;
	@Autowired
	private OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping(value="getCompanyList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getCompanyList(HttpServletRequest request,
			HttpServletResponse response){
		List<SysCompanyModel> companys = companyService.getSysCompList();
		return JsonUtil.toJson(companys);
	}
	
	/*
	 * 跳转到公司管理页面
	 */
	@RequestMapping("info_company.do")
	public ModelAndView info_company(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/company/companyInfo");
	        return mav;  
	}
	
	/*
	 * 获得公司列表的查询结果
	 */
	@RequestMapping(value="getgCompanyList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getgCompanyList(@ModelAttribute("model") CompanyModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//添加表头
		List<UiColumn> cols = companyService.getCols4List();
		//添加数据
		List<CompanyModel> list = companyService.getCompanyList(model);
		result.setCols(cols);
		result.setRows(list);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存修改
	 */
	@RequestMapping(value="saveCompanyChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveCompanyChange(@ModelAttribute("model") CompanyModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = companyService.saveCompanyChange(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 保存新增
	 */
	@RequestMapping(value="saveNewCompany.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewCompany(@ModelAttribute("model") CompanyModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = companyService.saveNewCompany(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 保存删除
	 */
	@RequestMapping(value="deleCompany.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleCompany(@ModelAttribute("model") CompanyModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = companyService.deleCompany(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 根据公司获得pcode树
	 */
	@RequestMapping(value="showCompanyTree4Pcode.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showCompanyTree4Pcode(@ModelAttribute("model") CompanyModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		list = companyService.showCompanyTree4Pcode(model);
		return JsonUtil.toJson(list);
	}
	/*
	 * 根据人员获得公司树
	 */
	
	@RequestMapping(value="showComList4Users.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showComList4Users(@ModelAttribute("model") SysUser model,HttpServletRequest request,
				HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		list = companyService.showComList4Users(model);
		return JsonUtil.toJson(list);
	}
	/*
	 * 根据站点获得公司树
	 */
	@RequestMapping(value="geCompanyListByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String geCompanyListByStation(@ModelAttribute("station") StationModel station,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		trees = companyService.geCompanyListByStation(station);
		return JsonUtil.toJson(trees);
	}
}
