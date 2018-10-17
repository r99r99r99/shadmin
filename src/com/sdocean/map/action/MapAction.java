package com.sdocean.map.action;

import java.util.ArrayList;
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
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.main.model.MainTenance;
import com.sdocean.map.model.MapModel;
import com.sdocean.map.service.MapService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Controller
public class MapAction {
	
	@Resource
	MapService mapService;
	@Autowired
	private ConfigInfo info;
	@Resource
	OperationLogService logService;
	/*
	 * 查询map表配置信息
	 */
	@RequestMapping(value="getMapConfigure.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getMapConfigure(HttpServletRequest request,
			HttpServletResponse response){
		MapModel map = new MapModel();
		map=mapService.getMapConfigure();
		return JsonUtil.toJson(map);
	}
	/*
	 * 跳转到地图编辑页面
	 */
	@RequestMapping("info_sysMap.do")
	public ModelAndView info_sysMap(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/system/mapInfo");
	        return mav;  
	}
	/*
	 * 地图页面初始化
	 */
	@RequestMapping(value="init_sysMap.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String init_sysMap(HttpServletRequest request,
			HttpServletResponse response){
		MapModel map = mapService.getMapConfigure();
		return JsonUtil.toJson(map);
	}
	
	/*
	 * 地图页面编辑保存
	 */
	@RequestMapping(value="saveMapConfig.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveMapConfig(@ModelAttribute("model") MapModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = mapService.saveMapConfig(model);
		return result.getMessage();
	}
}
