package com.sdocean.dataQuery.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.common.service.CommonService;
import com.sdocean.dataQuery.model.DataChangeModel;
import com.sdocean.dataQuery.model.DataCheckModel;
import com.sdocean.dataQuery.model.DataImportModel;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.ManualDataModel;
import com.sdocean.dataQuery.service.DataQueryService;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.indicator.service.IndicatorService;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.metadata.model.MetadataModel;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.dao.StationDao;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;

@Controller
public class DataQueryAction {

	private static Logger log = Logger.getLogger(DataQueryAction.class); 
	
	@Autowired
	DataQueryService dataQueryService;
	@Autowired
	IndicatorService indicatorService;
	@Autowired
	CommonService commonService;
	@Resource
	OperationLogService logService;
	@Autowired
	StationService stationService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("dataquery_init.do")
	public ModelAndView dataquery_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/dataquery_init");
	        return mav;  
	}
	
	@RequestMapping("dataqueryNow_init.do")
	public ModelAndView dataqueryNow_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/dataqueryNow_init");
	        return mav;  
	}
	/*
	 * 跳转到数据修改页面
	 */
	@RequestMapping("dataChange_init.do")
	public ModelAndView dataChange_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/dataChange_init");
	        return mav;  
	}
	/*
	 * 跳转到数据导入界面
	 */
	@RequestMapping("dataImport_init.do")
	public ModelAndView dataImport_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/dataImport_init");
	        return mav;  
	}
	/*
	 * 数据修改页面初始化
	 */
	@RequestMapping(value="dataChange_info.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String dataChange_info(HttpServletRequest request,
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
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);

		SysUser user = (SysUser) session.getAttribute("user");
		//获得用户权限下的站点的列表
		List<StationModel> stations = stationService.getStations4User(user);
		model.setStations(stations);
		
		//根据站点获得该站点下的参数列表
		/*List<SelectTree> indicatorTree = indicatorService.getIndicators4StationDevice4Show(station);
		model.setIndicatorTree(indicatorTree);*/
		
		return JsonUtil.toJson(model);
	}
	/*
	 * 为查询条件初始化
	 */
	@RequestMapping(value="dataquery_info.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String dataquery_info(HttpServletRequest request,
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
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 为设备管理查询结果
	 */
	@RequestMapping(value="showDataQueryInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showDataQueryInfo(@ModelAttribute("model") DataQueryModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = dataQueryService.getCols4DataQuery(model);
		result.setCols(cols);
		
		List<Map<String, Object>> rows = dataQueryService.getRows4DataQuery(model);
		result.setRows(rows);
		
		
		return JsonUtil.toJson(result);
	}
	/*
	 * 为数据查询中的数据修改提供查询结果
	 */
	@RequestMapping(value="showDataChangeInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showDataChangeInfo(@ModelAttribute("model") DataChangeModel model,HttpServletRequest request,
			HttpServletResponse response){
		StationModel station = stationService.getStationById(model.getStationId());
		model.setStation(station);
		List<DeviceModel> devices = commonService.indicatoridsToDevices(model.getIndicatorIds());
		for(DeviceModel device:devices){
			for(IndicatorModel indicator:device.getIndicators()){
				model.setDevice(device);
				model.setIndicator(indicator);
			}
		}
		//初始化表单结果
		PageResult page = new PageResult();
		List<UiColumn> cols = dataQueryService.getCols4DataChange(model);
		List<MetadataModel> rows = dataQueryService.getResult4DataChangeshow(model);
		page.setCols(cols);
		page.setRows(rows);
		model.setPage(page);
		return JsonUtil.toJson(model);
	}
	
	@RequestMapping(value="saveChangeMark.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeMark(@ModelAttribute("model") DataChangeModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员操作信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setUserId(user.getId());
		
		//进行修改用户信息操作
		Result result = dataQueryService.saveChangeMark(model);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	
	
	@RequestMapping(value="saveChangeData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveChangeData(@ModelAttribute("model") DataChangeModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员操作信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setUserId(user.getId());
		
		//进行修改用户信息操作
		Result result = dataQueryService.saveChangeData(model);
				
		logService.saveOperationLog(result,request);
				
		return result.getMessage();
	}
	
	@RequestMapping(value="saveImportData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveImportData(@ModelAttribute("model") DataImportModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得当前的人员信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		String newString = "["+model.getImportString()+"]";
		model.setImportString(newString);
		//对返回参数进行初始化
		Result result = dataQueryService.saveImportData(model,user);
		
		return result.getMessage();
	}
	/*
	 * 跳转到数据更改日志展示页面
	 */
	@RequestMapping("dataChangeLog_init.do")
	public ModelAndView dataChangeLog_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/dataChangeLog_init");
	        return mav;  
	}
	
	/*
	 * 数据更改日志查询页面初始化
	 */
	@RequestMapping(value="dataChangeLog_info.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String dataChangeLog_info(HttpServletRequest request,
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
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		//根据站点获得该站点下的参数列表
		List<SelectTree> indicatorTree = indicatorService.getIndicators4StationDevice4Show(station);
		model.setIndicatorTree(indicatorTree);
		
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 数据更改日志查询数据
	 */
	@RequestMapping(value="showdataChangeLog.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showdataChangeLog(@ModelAttribute("model") DataChangeModel model,HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		model.setStationId(station.getId());
		String indicatorIds = "";
		if(model.getIndicatorIds()!=null&&model.getIndicatorIds().length()>0){
			indicatorIds = model.getIndicatorIds();
			if(!indicatorIds.contains("#")){
				model.setDeviceId(Integer.parseInt(indicatorIds));
			}else{
				String indicatorid = indicatorIds.substring(0, indicatorIds.indexOf("#"));
				String deviceid = indicatorIds.substring(indicatorIds.indexOf("#")+1,indicatorIds.length());
				model.setIndicatorId(Integer.parseInt(indicatorid));
				model.setDeviceId(Integer.parseInt(deviceid));
			}
		}
		//初始化表单结果
		PageResult page = new PageResult();
		List<UiColumn> cols = dataQueryService.getDataChangeLogCols(model);
		List<DataChangeModel> rows = dataQueryService.getDataChangeLogRows(model);
		page.setCols(cols);
		page.setRows(rows);
		model.setPage(page);
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 根据站点以及设备,获得参数列表,并以表格的形式展示
	 */
	@RequestMapping(value="showColsByStationDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showColsByStationDevice(@ModelAttribute("model") DataImportModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		StationModel station = new StationModel();
		station.setId(model.getStationId());
		DeviceModel device = new DeviceModel();
		device.setId(model.getDeviceId());
		//为查询结果增加表头
		List<UiColumn> cols = dataQueryService.getColByStationDevice(station,device);
		result.setCols(cols);
		
		List<Map<String, Object>> rows = new ArrayList<>();
		result.setRows(rows);
		
		
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 跳转到检测外参数管理界面
	 */
	@RequestMapping("manualDataImport_init.do")
	public ModelAndView manualDataImport_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/manualDataImport_init");
	        return mav;  
	}
	/*
	 * 检测外参数管理界面初始化
	 */
	@RequestMapping(value="manualDataImport_info.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String manualDataImport_info(HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		ManualDataModel model = new ManualDataModel();
		//获得用户权限下的站点的列表
		List<StationModel> stations = stationService.getStations4User(user);
		model.setStations(stations);
		//获得所有参数的列表 以树的形式展示
		List<SelectTree> indicatorList = indicatorService.getAllIndicatorListTree4Single(null);
		model.setIndicators(indicatorList);
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    
		model.setBeginDate(beginDate);
		model.setEndDate(endDate);
		
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 查询手动导入数据
	 */
	@RequestMapping(value="showManualDataList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showManualDataList(@ModelAttribute("model") ManualDataModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = dataQueryService.getManualDataCols();
		result.setCols(cols);
		
		List<ManualDataModel> rows = dataQueryService.getManualDataList(model);
		result.setRows(rows);
		
		
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 手动导入数据
	 */
	@RequestMapping(value="saveManualDataList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveManualDataList(
			@RequestParam int stationId,
			@RequestParam String indicatorCode,
			@RequestParam String dataText,
			HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<ManualDataModel> models = dataQueryService.handleManualDataList(stationId, indicatorCode, dataText, user);
		result = dataQueryService.saveManualData(models);
		
		//logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 删除手动导入数据
	 */
	@RequestMapping(value="deleManualData.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleManualData(@ModelAttribute("model") ManualDataModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = dataQueryService.deleManualData(model);
		logService.saveOperationLog(result,request);
		return result.getMessage();
	}
	
	/*
	 * 跳转到数据审核页面
	 */
	@RequestMapping("dataCheck_init.do")
	public ModelAndView dataCheck_init(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/dataquery/dataCheck_init");
	        return mav;  
	}
	
	/*
	 * 在数据审核中,查询元数据列表
	 */
	@RequestMapping(value="showMetaDataListByStationIndicator.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showMetaDataListByStationIndicator(@ModelAttribute("model") DataCheckModel model,HttpServletRequest request,
			HttpServletResponse response){
		StationModel station = stationService.getStationById(model.getStationId());
		model.setStation(station);
		List<DeviceModel> devices = commonService.indicatoridsToDevices(model.getIndicatorIds());
		for(DeviceModel device:devices){
			for(IndicatorModel indicator:device.getIndicators()){
				model.setDevice(device);
				model.setIndicator(indicator);
			}
		}
		//初始化表单结果
		PageResult page = new PageResult();
		List<UiColumn> cols = dataQueryService.getCols4DataCheck();
		List<MetadataModel> rows = dataQueryService.getResult4DataCheckshow(model);
		page.setCols(cols);
		page.setRows(rows);
		model.setPage(page);
		return JsonUtil.toJson(model);
	}
	
	/*
	 * 保存数据审核结果
	 */
	@RequestMapping(value="saveDataCheckResult.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveDataCheckResult(@ModelAttribute("model") DataCheckModel model,HttpServletRequest request,
			HttpServletResponse response){
		//对返回参数进行初始化
		Result result = dataQueryService.saveDataCheckResult(model);
		return result.getMessage();
	}
}
