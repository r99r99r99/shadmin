package com.sdocean.position.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sdocean.frame.util.JsonUtil;
import com.sdocean.position.model.SysPosition;
import com.sdocean.position.service.SysPositionService;

@Controller
public class SysPositionAction {

	private static Logger log = Logger.getLogger(SysPositionAction.class);  
	@Autowired
	private SysPositionService positionService;
	
	@RequestMapping(value="getPositionList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getPositionList(HttpServletRequest request,
			HttpServletResponse response){
		List<SysPosition> positions = positionService.getPositionList();
		return JsonUtil.toJson(positions);
	}
	
}
