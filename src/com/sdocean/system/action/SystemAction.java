package com.sdocean.system.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.system.model.SystemWarnModel;
import com.sdocean.system.service.SystemService;
import com.sdocean.users.model.SysUser;

@Controller
public class SystemAction {
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	@Resource
	SystemService systemService;
	
	/*
	 * 定时读取用户的GPS告警通知
	 */
	@RequestMapping(value="getSystemWarn.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getSystemWarn(HttpServletRequest request,
			HttpServletResponse response){
		List<SystemWarnModel> list = new ArrayList<>();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		//读取GPS告警信息
		list = systemService.getGpsWarn(user);
		return JsonUtil.toJson(list);
	}
}
