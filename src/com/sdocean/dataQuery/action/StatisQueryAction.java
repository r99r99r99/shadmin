package com.sdocean.dataQuery.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.dataQuery.model.StatisModel;
import com.sdocean.dataQuery.service.StatisQueryService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;

@Controller
public class StatisQueryAction {

	private static Logger log = Logger.getLogger(StatisQueryAction.class); 
	
	@Autowired
	StatisQueryService statisService;
	@Autowired
	StationService stationService;
	
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_statisquery.do")
	public ModelAndView info_statisquery(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/statisquery_init");
		    
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
	 * 水质统计中查询
	 */
	@RequestMapping(value="/showStat.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String showStat(@ModelAttribute("gmodel") StatisModel gmodel,
						Map<String, Object> model, HttpServletRequest request,
						HttpServletResponse response) {
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		gmodel = statisService.getStatDataSearch(gmodel, station);
		gmodel.setStation(station);
		return JsonUtil.toJson(gmodel);
	}
	
	/*
	 * 首页限制折线图
	 */
	@RequestMapping(value="/showStat4First.do", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String showStat4First(@ModelAttribute("gmodel") StatisModel gmodel,
						Map<String, Object> model, HttpServletRequest request,
						HttpServletResponse response) {
		
		//获得USERID
		Long userId = (Long) request.getSession().getAttribute("userId");
		//获得站点信息
		StationModel station = stationService.getStationById(Integer.parseInt(gmodel.getStationId()));
		gmodel = statisService.getStatDataSearch4First(userId, gmodel, station);
		String result = JsonUtil.toJson(gmodel);
		return result;
	}	
	
}
