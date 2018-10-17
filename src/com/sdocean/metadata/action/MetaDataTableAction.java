package com.sdocean.metadata.action;

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
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.metadata.service.MetadataTableService;
import com.sdocean.page.model.NgColumn;
import com.sdocean.page.model.PageResult;
import com.sdocean.position.model.SysPosition;
import com.sdocean.position.service.SysPositionService;
import com.sdocean.role.model.RoleModel;
import com.sdocean.role.service.RoleService;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Controller
public class MetaDataTableAction {

	private static Logger log = Logger.getLogger(MetaDataTableAction.class);  
	@Autowired
	private MetadataTableService metaService;
	@Resource
	OperationLogService logService;
	
	/*
	 * 获得角色的列表
	 */
	@RequestMapping(value="makeMetaTable4Station.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String makeMetaTable4Station(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = metaService.makeMetaTable4Station(model, 4);
		return result.getMessage();
	}
	
}
