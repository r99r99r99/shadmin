package com.sdocean.pollutant.action;

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

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.service.CommonService;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.service.IndicatorService;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.pollutant.model.PollutantModel;
import com.sdocean.pollutant.service.PollutantService;
import com.sdocean.station.model.StationModel;

@Controller
public class PollutantAction {

	private static Logger log = Logger.getLogger(PollutantAction.class); 
	
	@Autowired
	PollutantService pollutantService;
	@Autowired
	IndicatorService indicatorService;
	@Autowired
	CommonService commonService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_pollutant.do")
	public ModelAndView info_pollutant(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/pollutant/pollutant_init");
	        return mav;  
	}
	/*
	 * 初始化入海污染量
	 */
	@RequestMapping(value="pollutant_init.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String pollutant_init(HttpServletRequest request,
			HttpServletResponse response){
		PollutantModel model = new PollutantModel();
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.DATE, -1);
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    model.setBeginDate(beginDate);
	    model.setEndDate(endDate);
	    //获得参数列表
	    List<SelectTree> indicators = indicatorService.getIndicators4Pollu(station);
	    model.setIndicatorTree(indicators);
	    //初始化统计口径
	    model.setType(1);
	    //初始化统计单位
	    model.setUnit("kg");
		return JsonUtil.toJson(model);
	}
	/*
	 * 入海污染量查询
	 */
	@RequestMapping(value="showPollutantInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showPollutantInfo(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//对获得的参数indicatorids进行初始化
		List<DeviceModel> devices = commonService.indicatoridsToDevices(model.getIndicatorIds());
		model.setDevices(devices);
		//获得session中的station信息
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		model.setWpId(station.getId());
		//为查询结果增加表头
		List<UiColumn> cols = pollutantService.getColumns4Polluinfo(model);
		result.setCols(cols);
		List<Map<String, Object>> rows = pollutantService.getRows4Polluinfo(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 为首页展示入海污染量查询
	 */
	@RequestMapping(value="showPollutantInfo4First.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showPollutantInfo4First(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//获得session中的station信息
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		model.setWpId(station.getId());
		List<DeviceModel> devices = pollutantService.getDevices4Pollu(station);
		model.setDevices(devices);
		model.setType(1);
		model.setUnit("t");
		
		List<UiColumn> cols = pollutantService.getColumns4Polluinfo(model);
		result.setCols(cols);
		List<Map<String, Object>> rows = pollutantService.getRows4Polluinfo(model);
		result.setRows(rows);
		
		return JsonUtil.toJson(result);
	}
	/*
	 * 初始化入海污染量设置
	 */
	@RequestMapping("info_pollutantset.do")
	public ModelAndView info_pollutantset(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/pollutant/pollutantsetInfo");
		
		return mav;
	}
	/*
	 * 入海污染量设置查询
	 */
	@RequestMapping(value="showPolluSetList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showPolluSetList(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		List<UiColumn> cols = pollutantService.getColumns4PolluSetinfo(model);
		List<PollutantModel> rows = pollutantService.getPolluSetList(model);
		result.setCols(cols);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 新增入海污染源设置
	 */
	@RequestMapping(value="saveNewPolluSet.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewPolluSet(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = pollutantService.saveNewPolluSet(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 修改入海污染源设置
	 */
	@RequestMapping(value="savePolluSet.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String savePolluSet(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = pollutantService.savePolluSet(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 删除入海污染源设置
	 */
	@RequestMapping(value="delePolluSet.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String delePolluSet(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = pollutantService.delePolluSet(model);
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 入海污染量设置查询
	 */
	@RequestMapping(value="showPolluSetList4First.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showPolluSetList4First(@ModelAttribute("model") PollutantModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		List<UiColumn> cols = pollutantService.getColumns4Polluinfo4First();
		//List<PollutantModel> rows = pollutantService.getPolluSetList(model);
		result.setCols(cols);
		return JsonUtil.toJson(result);
	}
}
