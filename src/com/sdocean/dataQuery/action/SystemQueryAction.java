package com.sdocean.dataQuery.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.SystemQueryModel;
import com.sdocean.dataQuery.service.SystemQueryService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Controller
public class SystemQueryAction {

	private static Logger log = Logger.getLogger(SystemQueryAction.class); 
	
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	@Autowired
	SystemQueryService systemQueryService;
	
	/*
	 * 系统状态查询
	 */
	@RequestMapping("systemQuery_init.do")
	public ModelAndView systemQuery_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/systemQuery_init");
	        return mav;  
	}
	/*
	 * 为查询条件初始化
	 */
	@RequestMapping(value="systemQuery_info.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String systemQuery_info(HttpServletRequest request,
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
	    calendar.add(Calendar.DATE, -2);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		return JsonUtil.toJson(model);
	}
	
	@RequestMapping(value="showSystemQueryInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showSystemQueryInfo(@ModelAttribute("model") SystemQueryModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = systemQueryService.getCols4SystemQuery(model);
		result.setCols(cols);
		
		List<Map<String, Object>> rows = systemQueryService.getRows4SystemQuery(model);
		result.setRows(rows);
		
		return JsonUtil.toJson(result);
	}
}
