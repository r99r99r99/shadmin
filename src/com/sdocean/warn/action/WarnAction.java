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
import com.sdocean.warn.model.WarnModel;
import com.sdocean.warn.model.WarnValueModel;
import com.sdocean.warn.service.WarnService;

@Controller
public class WarnAction {
	@Resource
	WarnService warnService;
	@Resource
	OperationLogService logService;
	@Resource
	StationService stationService;
	@Resource
	PublicService publicService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_warnsetting.do")
	public ModelAndView handleRequestInternal(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/warn/warnSettingInfo");
	        return mav;  
	}
	
	@RequestMapping(value="showWarnSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showWarnSetting(@ModelAttribute("model") WarnModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		
		PageResult result = new PageResult();
		//为查询结果添加表头
		List<UiColumn> cols = warnService.getCols4List();
		result.setCols(cols);
		//为查询结果添加结果
		List<WarnModel> rows = warnService.getWarnList(model, user);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	@RequestMapping(value="saveWarnChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveWarnChange(@ModelAttribute("model") WarnModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = warnService.saveWarnChange(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	@RequestMapping(value="saveNewWarn.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewWarn(@ModelAttribute("model") WarnModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = warnService.saveNewWarn(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	@RequestMapping(value="deleWarnSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleWarnSetting(@ModelAttribute("model") WarnModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = warnService.deleWarnSetting(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	/*
	 * 跳转到预警告警信息页面
	 */
	@RequestMapping("info_warnvalue.do")
	public ModelAndView info_warnvalue(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/warn/warnValueInfo");
	        return mav;  
	}
	
	/*
	 * 初始化预警告警信息页面
	 */
	@RequestMapping(value="init_warnvalue.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String init_warnvalue(HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		WarnValueModel model = new WarnValueModel();
		//根据人员权限初始化站点列表
		List<StationModel> stationList = stationService.getStations4User(user);
		List<StationModel> stations = new ArrayList<StationModel>();
		StationModel allStation = new StationModel();
		allStation.setId(0);
		allStation.setTitle("全部站点");
		stations.add(allStation);
		stations.addAll(stationList);
		//初始化类型列表
		List<PublicModel> publics = publicService.getPublicsByParent("0008");
		List<PublicModel> typeList = new ArrayList<PublicModel>();
		PublicModel type = new PublicModel();
		type.setClassId(0);
		type.setValue("全部");
		typeList.add(type);
		typeList.addAll(publics);
		//初始化查询时间
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
	    model.setStationList(stations);
	    model.setTypeList(typeList);
	    model.setBeginDate(beginDate);
	    model.setEndDate(endDate);
		return JsonUtil.toJson(model);
	}
	/*
	 * 展示预警告警信息
	 */
	@RequestMapping(value="showWarnValues.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showWarnValues(@ModelAttribute("model") WarnValueModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		
		PageResult result = new PageResult();
		//为查询结果添加表头
		List<UiColumn> cols = warnService.getCols4WarnValue();
		result.setCols(cols);
		//为查询结果添加结果
		List<WarnValueModel> rows = warnService.getWarnValueRows(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 保存操作信息
	 */
	@RequestMapping(value="saveOperaValue.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveOperaValue(@ModelAttribute("model") WarnValueModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = warnService.saveOperaValue(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 预警告警信息批量修改操作信息
	 */
	@RequestMapping(value="updateAllOperaValue.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateAllOperaValue(@ModelAttribute("model") WarnValueModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = warnService.updateAllOperaValue(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
}
