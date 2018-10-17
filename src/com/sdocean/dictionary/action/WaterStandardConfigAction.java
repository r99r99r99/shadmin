package com.sdocean.dictionary.action;

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

import com.sdocean.dictionary.model.WaterStandardConfig;
import com.sdocean.dictionary.service.WaterStandardConfigService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;

@Controller
public class WaterStandardConfigAction {
	@Resource
	WaterStandardConfigService waterStandardConfigService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	@Autowired
	private StationService stationService;
	
	/*
	 * 查询水质等级配置列表
	 */
	@RequestMapping(value="getWaterStandardConfigList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getWaterStandardConfigList(@ModelAttribute("model") WaterStandardConfig model,HttpServletRequest request,
			HttpServletResponse response){
		List<WaterStandardConfig> rows = waterStandardConfigService.getWaterStandardConfigList(model);
		return JsonUtil.toJson(rows);
	}
	
	/*
	 * 获得当前站点所属的水质等级配置
	 */
	@RequestMapping(value="getWaterStandardConfigListByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getWaterStandardConfigListByStation(HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		WaterStandardConfig model = new WaterStandardConfig();
		model.setTypeId(station.getWaterType());
		List<WaterStandardConfig> rows = waterStandardConfigService.getWaterStandardConfigList(model);
		return JsonUtil.toJson(rows);
	}
	
	/*
	 * 根据站点编码,获得该站点所属的水质等级配置
	 */
	@RequestMapping(value="getWaterStandardConfigListByStationId.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getWaterStandardConfigListByStationCode(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		StationModel station = stationService.getStationById(model.getId());
		WaterStandardConfig wsc = new WaterStandardConfig();
		wsc.setTypeId(station.getWaterType());
		List<WaterStandardConfig> rows = waterStandardConfigService.getWaterStandardConfigList(wsc);
		return JsonUtil.toJson(rows);
	}
}
