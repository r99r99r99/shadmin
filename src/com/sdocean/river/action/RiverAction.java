package com.sdocean.river.action;

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
import com.sdocean.common.model.SelectTree;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.river.model.RiverModel;
import com.sdocean.river.service.RiverService;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;

@Controller
public class RiverAction {

	private static Logger log = Logger.getLogger(RiverAction.class);  
	@Autowired
	private RiverService riverService;
	@Autowired
	private StationService stationService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	/*
	 * 跳转到流域管理界面
	 */
	@RequestMapping("info_river.do")
	public ModelAndView info_river(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/river/riverInfo");
	        return mav;  
	}
	
	/*
	 * 查询流域列表
	 */
	@RequestMapping(value="showRiverList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showRiverList(@ModelAttribute("model") RiverModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = riverService.getCols4RiverList();
		result.setCols(cols);
		//为查询结果增加列表
		List<RiverModel> rows = riverService.getRiverListByRiver(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存流域新增信息
	 */
	@RequestMapping(value="saveRiver.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveRiver(@ModelAttribute("model") RiverModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<StationModel> stations = stationService.getStationListByIds(model.getStationIds());
		model.setStations(stations);
		Result result = new Result();
		result = riverService.saveRiver(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 修改流域信息
	 */
	@RequestMapping(value="updateRiver.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String updateRiver(@ModelAttribute("model") RiverModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<StationModel> stations = stationService.getStationListByIds(model.getStationIds());
		model.setStations(stations);
		Result result = new Result();
		result = riverService.updateRiver(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 获得流域包含的站点列表
	 */
	@RequestMapping(value="getRiverStationSelectTree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getRiverStationSelectTree(@ModelAttribute("model") RiverModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = riverService.getRiverStationSelectTree(model);
		
		return JsonUtil.toJson(list);
	}
}