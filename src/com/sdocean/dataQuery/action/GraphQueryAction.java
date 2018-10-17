package com.sdocean.dataQuery.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.sdocean.common.model.Echarts;
import com.sdocean.common.model.HchartsLineData;
import com.sdocean.common.model.HchartsServieModel;
import com.sdocean.common.model.PlotLine;
import com.sdocean.common.model.SelectTree;
import com.sdocean.dataQuery.model.ComparisonData;
import com.sdocean.dataQuery.model.ComparisonModel;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.GraphModel;
import com.sdocean.dataQuery.service.GraphQueryService;
import com.sdocean.dictionary.service.WaterQualityStandardService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.indicator.service.IndicatorService;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;

@Controller
public class GraphQueryAction {

	private static Logger log = Logger.getLogger(GraphQueryAction.class); 
	
	@Autowired
	IndicatorService indicatorService;
	
	@Autowired
	GraphQueryService graphService;
	
	@Autowired
	private ConfigInfo info;
	
	@Autowired
	StationService stationService;
	
	@Autowired
	WaterQualityStandardService waterQualityStandardService;
	
	@RequestMapping("info_graphquery.do")
	public ModelAndView info_graphquery(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/graphquery_init");
		    
		    DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			//默认开始时间为一个月以前
			calendar.add(Calendar.MONTH, -2);
			// 添加默认开始时间
			String time1 = beginDf.format(calendar.getTime()) ;
			// 添加默认结束时间
			String time2 = df.format(new Date());
			// 存放参数
			mav.addObject("beginDate", time1);
			mav.addObject("endDate", time2);
	        return mav;  
	}
	
	/*
	 * 为查询条件初始化
	 */
	@RequestMapping(value="graphquery_init.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String graphquery_init(HttpServletRequest request,
			HttpServletResponse response){
		DataQueryModel model = new DataQueryModel();
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		model.setStationId(station.getId());
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.DATE, -20);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		//根据站点获得该站点下的参数列表
		List<SelectTree> indicatorTree = indicatorService.getIndicators4StationDevice4Show(station);
		model.setIndicatorTree(indicatorTree);
		
		return JsonUtil.toJson(model);
	}
	/*
	 * 数据评价中综合趋势展示图标
	 */
	@RequestMapping(value="graphShow4echarts.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String graphShow4echarts(@ModelAttribute("model") GraphModel model,HttpServletRequest request,
			HttpServletResponse response) {
		//对参数进行初始化
		graphService.modelInfo(model);
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		List<HchartsServieModel> list= graphService.getEcharts4Graph(station, model);
		System.out.println(JsonUtil.toJson(list));
		return JsonUtil.toJson(list);
	}
	
	/*
	 * 跳转到综合对比页面
	 * 综合对比用于站点间单参数的折现对比
	 */
	@RequestMapping("info_comparison.do")
	public ModelAndView info_comparison(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/comparison_init");
		    
		    DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			//默认开始时间为一个月以前
			calendar.add(Calendar.MONTH, -2);
			// 添加默认开始时间
			String time1 = beginDf.format(calendar.getTime()) ;
			// 添加默认结束时间
			String time2 = df.format(new Date());
			// 存放参数
			mav.addObject("beginDate", time1);
			mav.addObject("endDate", time2);
	        return mav;  
	}
	
	/*
	 *为综合对比查询提供初始化 
	 */
	@RequestMapping(value="comparison_init.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String comparison_init(HttpServletRequest request,
			HttpServletResponse response){
		ComparisonModel model = new ComparisonModel();
		HttpSession session = request.getSession();
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.DATE, -20);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		//获得当前用户的登录信息
		SysUser user = (SysUser) session.getAttribute("user");
		//根据站点获得该站点下的参数列表
		List<SelectTree> stationTree = stationService.getStationTreeListByUser(user);
		model.setStationTree(stationTree);
		return JsonUtil.toJson(model);
	}
	/*
	 * 通过站点列表查询出站点下的参数列表
	 */
	@RequestMapping(value="getIndicatorTreeByStations.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorTreeByStations(@ModelAttribute("model") ComparisonModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		//根据站点ids获得站点列表的信息
		List<StationModel> stations = stationService.getStationListByIds(model.getStationIds());
		//根据站点列表获得参数列表
		list = indicatorService.getIndicatorTreesByStations(stations);
		return JsonUtil.toJson(list);
	}
	
	/*
	 * 为综合对比查询提供结果
	 */
	@RequestMapping(value="getComparisonResult.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getComparisonResult(@ModelAttribute("model") ComparisonModel model,HttpServletRequest request,
			HttpServletResponse response){
		//格式化参数
		List<StationModel> stations = stationService.getStationListByIds(model.getStationIds());
		model.setStations(stations);
		IndicatorModel indicator = indicatorService.getIndicatorByCode(model.getIndicatorCode());
		model.setIndicator(indicator);
		
		ComparisonModel result = graphService.getComparisonResult(model);
		//根据站点以及参数,获得水质标准的提醒
		if(stations!=null&&stations.size()>0){
			List<PlotLine> plotLines = waterQualityStandardService.getPlotLines(stations.get(0), indicator.getCode());
			result.setPlotLines(plotLines);
		}
		return JsonUtil.toJson(result);
	}
}
