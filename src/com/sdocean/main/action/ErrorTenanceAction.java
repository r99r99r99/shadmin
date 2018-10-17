package com.sdocean.main.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.ErrorManager;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.file.action.FileUpload;
import com.sdocean.file.model.SysFile;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.main.model.ErrorTenance;
import com.sdocean.main.model.MainTenance;
import com.sdocean.main.model.MainTenanceFile;
import com.sdocean.main.service.ErrorTenanceService;
import com.sdocean.main.service.MainTenanceService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;

@Controller
public class ErrorTenanceAction {
	@Resource
	ErrorTenanceService errorSevice;
	@Resource
	OperationLogService logService;
	@Resource
	StationService stationService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_errorEdit.do")
	public ModelAndView info_errorEdit(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/main/errorEditInfo");
	        return mav;  
	}
	
	/*
	 * 站点异常查询
	 */
	@RequestMapping("info_errorShow.do")
	public ModelAndView info_errorShow(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/main/errorShowInfo");
	        return mav;  
	}
	/*\
	 * 初始化站点异常维护上报查询条件
	 */
	@RequestMapping(value="init_errorEdit.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String init_errorEdit(@ModelAttribute("model") ErrorTenance model,HttpServletRequest request,
			HttpServletResponse response){
		ErrorTenance err = new ErrorTenance();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		//获得权限内的站点的列表
		List<StationModel> stations = stationService.getStations4User(user);
		//初始化查询时间
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
	    err.setStations(stations);
	    err.setBeginTime(beginDate);
	    err.setEndTime(endDate);
		return JsonUtil.toJson(err);
	}
	/*
	 * 查询列表
	 */
	@RequestMapping(value="showErrorEditList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showErrorEditList(@ModelAttribute("model") ErrorTenance model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		HttpSession session = request.getSession();
		List<StationModel> stations = (List<StationModel>) session.getAttribute("stations");
		List<UiColumn> cols = errorSevice.getCols4MainEditList();
		List<ErrorTenance> rows = errorSevice.getErrorsByStation(model, stations);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 保存新增
	 */
	@RequestMapping(value="saveNewErrorTenance.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewErrorTenance(@ModelAttribute("model") ErrorTenance model,HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setUserId(user.getId());
		Result result = errorSevice.saveNewErrorTenance(model);
		return result.getMessage();
	}
	/*
	 * 保存修改
	 */
	@RequestMapping(value="saveChangeErrorTenance.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeErrorTenance(@ModelAttribute("model") ErrorTenance model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = errorSevice.saveChangeErrorTenance(model);
		return result.getMessage();
	}
	/*
	 * 为异常维护提供设备列表
	 */
	@RequestMapping(value="getDeviceList4Error.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDeviceList4Error(@ModelAttribute("model") ErrorTenance model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		list = errorSevice.getDeviceList4Error(model);
		return JsonUtil.toJson(list);
	}
}
