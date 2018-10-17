package com.sdocean.dataQuery.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.sdocean.common.model.PlotLine;
import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.service.CommonService;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.MouldModel;
import com.sdocean.dataQuery.model.StatisReportLineData;
import com.sdocean.dataQuery.model.StatisReportModel;
import com.sdocean.dataQuery.service.MouldService;
import com.sdocean.dataQuery.service.StatisReportService;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.service.WaterQualityStandardService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.indicator.service.IndicatorService;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;

@Controller
public class StatisReportAction {

	private static Logger log = Logger.getLogger(StatisReportAction.class); 
	
	@Autowired
	OperationLogService logService;
	@Autowired
	IndicatorService indicatorService;
	@Autowired
	StationService stationService;
	@Autowired
	CommonService commonService;
	@Autowired
	StatisReportService statisReportService;
	@Autowired
	WaterQualityStandardService waterQualityStandardService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_statisreportavg.do")
	public ModelAndView info_statisreportavg(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/statisreportavg_info");
	        return mav;  
	}
	/*
	 * 为查询条件初始化
	 */
	@RequestMapping(value="statisreportavg_init.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String statisreportavg_init(HttpServletRequest request,
			HttpServletResponse response){
		StatisReportModel model = new StatisReportModel();
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		model.setStationId(station.getId());
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.MONTH, -3);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		//根据站点获得该站点下的参数列表
		List<SelectTree> indicatorTree = indicatorService.getIndicators4StationDevice4Show(station);
		model.setIndicatorTree(indicatorTree);
		
		//获得统计类型的列表
		List<SelectTree> statTypeTrees = statisReportService.getSelectTree4StatType();
		model.setStatTypeTree(statTypeTrees);
		
		//初始化查询口径
		model.setCollectType(2);
		
		return JsonUtil.toJson(model);
	}
	/*
	 * 为综合查询提供结果
	 */
	@RequestMapping(value="statisreportavg_show.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String statisreportavg_show(@ModelAttribute("model") StatisReportModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//对参数进行初始化
		List<DeviceModel> devices = commonService.indicatoridsToDevices(model.getIndicatorIds());
		model.setDevices(devices);
		//根据站点的ID获得站点信息
		StationModel station = stationService.getStationById(model.getStationId());
		model.setStation(station);
		
		//为查询结果增加表头
		List<UiColumn> cols = statisReportService.getCols4StatisReportAvg(model);
		//为查询结果提供结果集
		List<StatisReportModel> rows = statisReportService.getRowsStatisReportDatas(model);
		result.setCols(cols);
		result.setRows(rows);
		model.setPageResult(result);
		
		List<StatisReportLineData> lines = statisReportService.changeRows2Line4StatisReport(rows, model);
		model.setDatas(lines);
		
		//根据站点以及参数,获得水质标准的提醒
		IndicatorModel indicator = devices.get(0).getIndicators().get(0);
		model.setIndicator(indicator);
		List<PlotLine> plotLines = waterQualityStandardService.getPlotLines(station, indicator.getCode());
		model.setPlotLines(plotLines);
		
		
		return JsonUtil.toJson(model);
	}
	
}