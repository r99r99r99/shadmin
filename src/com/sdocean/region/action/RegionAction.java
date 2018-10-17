package com.sdocean.region.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sdocean.common.model.SelectTree;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.region.service.RegionService;
import com.sdocean.station.model.StationModel;

@Controller
public class RegionAction {

	private static Logger log = Logger.getLogger(RegionAction.class);  
	@Autowired
	private RegionService regionService;
	
	@RequestMapping(value="geRegionList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String geRegionList(@ModelAttribute("station") StationModel station,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		trees = regionService.getRegionList4Tree(station);
		return JsonUtil.toJson(trees);
	}
}
