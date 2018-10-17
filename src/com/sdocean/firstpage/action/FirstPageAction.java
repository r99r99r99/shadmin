package com.sdocean.firstpage.action;

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
import com.sdocean.dataQuery.service.DataQueryService;
import com.sdocean.dataQuery.service.MouldService;
import com.sdocean.dataQuery.service.StatisQueryService;
import com.sdocean.firstpage.model.FirstPageModel;
import com.sdocean.firstpage.model.FirstPageShow;
import com.sdocean.firstpage.model.LastMetaData;
import com.sdocean.firstpage.model.SystemModel;
import com.sdocean.firstpage.model.WaterStandard;
import com.sdocean.firstpage.service.FirstPageService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.main.model.MainTenance;
import com.sdocean.main.service.MainTenanceService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.pollutant.model.Pollutant4First;
import com.sdocean.pollutant.model.PollutantModel;
import com.sdocean.pollutant.service.PollutantService;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationStatusModel;
import com.sdocean.station.service.StationService;
import com.sdocean.warn.model.Warn4FirstModel;
import com.sdocean.warn.service.WarnService;

@Controller
public class FirstPageAction {

	private static Logger log = Logger.getLogger(FirstPageAction.class); 
	
	@Autowired
	DataQueryService dataQueryService;
	@Autowired
	StatisQueryService statisQueryService;
	@Autowired
	WarnService warnService;
	@Autowired
	FirstPageService firstPageService;
	@Resource
	OperationLogService logService;
	@Resource
	MouldService mouldService;
	@Resource
	PollutantService pollutantService;
	@Resource
	MainTenanceService mainService;
	@Autowired
	private ConfigInfo info;
	@Autowired
	private StationService stationService;
	
	@RequestMapping("firstPage.do")
	public ModelAndView firstPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/home");
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		MouldModel result = mouldService.getMouldByStation(station);
		mav.addObject("mould", result.getMould());
		return mav;
	}
	
	@RequestMapping(value="/getlastdata.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getlastdata(HttpServletRequest request, HttpServletResponse response){
		//初始化返回值
		FirstPageModel firstPage = new FirstPageModel();
		//获得当前登录的站点信息
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		//获得实时数据列表
		/*List<LastMetaData> list = new ArrayList<LastMetaData>(); 
		list = dataQueryService.getData4FirstPage(station);*/
		//获得当前的水质等级
		WaterStandard waterStandard = statisQueryService.getWaterStandard(station);
		//初始化参数,获得当前时间一天前的时间
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//默认开始时间为一个月以前
		calendar.add(Calendar.DATE, -1);
		String beginDate = beginDf.format(calendar.getTime());
		//获得预警信息
		Warn4FirstModel warn = warnService.getWarn4First(Long.parseLong("2"), station, beginDate);
		//获得告警信息
		Warn4FirstModel alarm = warnService.getWarn4First(Long.parseLong("1"), station, beginDate);
		//获得系统状态
		List<SystemModel> system = firstPageService.getSystemList4Firstpage(station);
		//获得入海污染量
		//Pollutant4First first = pollutantService.getPollutants4FirstPage(station);
		//获得站点内设备下次维护时间
		List<MainTenance> mains = new ArrayList<MainTenance>();
		mains = mainService.getPlanMain4FirstPage(station);
		//firstPage.setDatas(list);
		firstPage.setWaterStandard(waterStandard);
		firstPage.setWarn(warn);
		firstPage.setAlarm(alarm);
		firstPage.setSystem(system);
		//firstPage.setPollutant(first);
		firstPage.setMains(mains);
		return JsonUtil.toJson(firstPage);
	}
	@RequestMapping(value="/getlastdataNow.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getlastdataNow(HttpServletRequest request, HttpServletResponse response){
		//初始化返回值
		FirstPageModel firstPage = new FirstPageModel();
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		//获得实时数据列表
		List<LastMetaData> list = new ArrayList<LastMetaData>(); 
		list = dataQueryService.getData4FirstPage(station);
		firstPage.setDatas(list);
		return JsonUtil.toJson(firstPage);
	}
	
	/*
	 * 为首页读取展示数据
	 */
	@RequestMapping(value="/getDatas4Firstpage.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getDatas4Firstpage(@ModelAttribute("model") FirstPageModel model,HttpServletRequest request, HttpServletResponse response){
		//初始化返回值
		FirstPageModel firstPage = new FirstPageModel();
		StationModel station = stationService.getStationById(model.getStationId());
		//获得实时数据列表
		List<LastMetaData> list = new ArrayList<LastMetaData>(); 
		list = dataQueryService.getData4FirstPage(station);
		//获得当前的水质等级
		WaterStandard waterStandard = statisQueryService.getWaterStandard(station);
		
		firstPage.setDatas(list);
		firstPage.setWaterStandard(waterStandard);
		return JsonUtil.toJson(firstPage);
	}
	
	@RequestMapping(value="/getFistWaterStandard.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String getWaterStandard(HttpServletRequest request, HttpServletResponse response){
		//获得当前登录的站点信息
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		//获得当前的水质等级
		WaterStandard waterStandard = statisQueryService.getWaterStandard(station);
		return JsonUtil.toJson(waterStandard);
	}
	
	/*
	 * 跳转到首页实时数据设置页面
	 */
	@RequestMapping("info_firstpageshow.do")
	public ModelAndView info_firstpageshow(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/firstpage/firstpageshowInfo");
		
		return mav;
	}
	/*
	 * 为参数组管理查询结果
	 */
	@RequestMapping(value="showfirstpageshow.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showfirstpageshow(@ModelAttribute("model") FirstPageShow model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = firstPageService.getCols4List();
		result.setCols(cols);
		
		List<FirstPageShow> rows = firstPageService.getFirstPageShowList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 *  保存修改首页实时数据管理
	 */
	@RequestMapping(value="saveFirstPageShowChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveFirstPageShowChange(@ModelAttribute("model") FirstPageShow model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = firstPageService.saveFirstPageShowChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 *  新增修改首页实时数据管理
	 */
	@RequestMapping(value="saveNewFirstPageShow.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewFirstPageShow(@ModelAttribute("model") FirstPageShow model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = firstPageService.saveNewFirstPageShow(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 *  删除首页实时数据管理
	 */
	@RequestMapping(value="deleFirstPageShowSetting.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleFirstPageShowSetting(@ModelAttribute("model") FirstPageShow model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = firstPageService.deleFirstPageShowSetting(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 根据站点获得当前站点的水质评价模板
	 */
	@RequestMapping(value="getMouldByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getMouldByStation(HttpServletRequest request,
			HttpServletResponse response){
		//获得当前登录的站点信息
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		MouldModel result = mouldService.getMouldByStation(station);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 跳转到首页系统运行状态设置页面
	 */
	@RequestMapping("info_firstpagesystem.do")
	public ModelAndView info_firstpagesystem(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/firstpage/firstpagesystemInfo");
		
		return mav;
	}
	/*
	 * 查询首页系统运行状态设置列表
	 */
	@RequestMapping(value="showSystemModelList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showSystemModelList(@ModelAttribute("model") SystemModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		List<UiColumn> cols = firstPageService.getCols4System();
		List<SystemModel> rows = firstPageService.getSystemModelList(model);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 添加首页系统运行状态设置
	 */
	@RequestMapping(value="addSystemModel.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String addSystemModel(@ModelAttribute("model") SystemModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = firstPageService.addSystemModel(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 删除首页系统运行状态设置
	 */
	@RequestMapping(value="deleSystemModel.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleSystemModel(@ModelAttribute("model") SystemModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = firstPageService.deleSystemModel(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 修改首页系统运行状态设置
	 */
	@RequestMapping(value="updateSystemModel.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateSystemModel(@ModelAttribute("model") SystemModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = firstPageService.updateSystemModel(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 获得该站点的可监测状态的参数列表，以及传感器列表
	 */
	@RequestMapping(value="getSystemModelList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getSystemModelList(HttpServletRequest request,
			HttpServletResponse response){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		//获得session中的station
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		list = firstPageService.getSystemModelList4Set(station);
		return JsonUtil.toJson(list);
	}
	
	/*
	 * 获得所有站点的状态
	 */
	@RequestMapping(value="getStationStatusList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationStatusList(HttpServletRequest request,
			HttpServletResponse response){
		List<StationStatusModel> list = new ArrayList<StationStatusModel>();
		//获得权限中的站点列表
		HttpSession session = request.getSession();
		List<StationModel> stations = (List<StationModel>) session.getAttribute("stations");
		for(StationModel station:stations){
			StationStatusModel stationStatus = stationService.getStationStatus(station);
			list.add(stationStatus);
		}
		return JsonUtil.toJson(list);
	}
	
	/*
	 * 获得某一个站点的状态
	 */
	@RequestMapping(value="getStationStatus.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationStatus(@ModelAttribute("model") StationModel station,HttpServletRequest request,
			HttpServletResponse response){
		StationStatusModel stationStatus = stationService.getStationStatus(station);
		return JsonUtil.toJson(stationStatus);
	}
}
