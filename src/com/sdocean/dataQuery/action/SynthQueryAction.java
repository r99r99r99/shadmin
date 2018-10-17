package com.sdocean.dataQuery.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.sdocean.common.model.SelectTree;
import com.sdocean.common.service.CommonService;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.service.SynthQueryService;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.service.IndicatorService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Controller
public class SynthQueryAction {

	private static Logger log = Logger.getLogger(SynthQueryAction.class); 
	
	@Autowired
	SynthQueryService synthQueryService;
	
	@Autowired
	IndicatorService indicatorService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_synthquery.do")
	public ModelAndView info_synthquery(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/synthquery_info");
		    
	        return mav;  
	}
	
	/*
	 * 为查询条件初始化
	 */
	@RequestMapping(value="synthquery_init.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String synthquery_init(HttpServletRequest request,
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
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		//根据站点获得该站点下的参数列表
		List<SelectTree> indicatorTree = indicatorService.getIndicators4StationDevice4Show(station);
		model.setIndicatorTree(indicatorTree);
		
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 为综合查询提供结果
	 */
	@RequestMapping(value="synthquery_show.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String synthquery_show(@ModelAttribute("model") DataQueryModel model,HttpServletRequest request,
			HttpServletResponse response){
		
		PageResult result = new PageResult();
		//对参数进行初始化
		List<DeviceModel> devices = commonService.indicatoridsToDevices(model.getIndicatorIds());
		model.setDevices(devices);
		//为查询结果增加表头
		List<UiColumn> cols = synthQueryService.getCols4SynQuery(model);
		result.setCols(cols);
		
		//为查询结果增加结果集
		List<Map<String, Object>> rows = synthQueryService.getRows4SynQuery(model);
		result.setRows(rows);
		
		return JsonUtil.toJson(result);
	}
}
