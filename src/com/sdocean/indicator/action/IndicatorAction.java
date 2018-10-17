package com.sdocean.indicator.action;

import java.util.ArrayList;
import java.util.List;

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
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorGroupModel;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.indicator.service.IndicatorService;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Controller
public class IndicatorAction {

	private static Logger log = Logger.getLogger(IndicatorAction.class);  
	@Autowired
	private IndicatorService indicatorService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	
	@RequestMapping("info_indicator.do")
	public ModelAndView info_indicator_group(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/indicator/indicatorInfo");
	        return mav;  
	}
	/*
	 * 为参数组管理查询结果
	 */
	@RequestMapping(value="showIndicators.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showIndicators(@ModelAttribute("model") IndicatorModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = indicatorService.getCols4List();
		result.setCols(cols);
		
		List<IndicatorModel> rows = indicatorService.showIndicators(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	/*
	 * 获得参数列表
	 */
	@RequestMapping(value="getIndicatorList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorList(@ModelAttribute("model") IndicatorModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<IndicatorModel> rows = indicatorService.showIndicators(model);
		return JsonUtil.toJson(rows);
	}
	
	/*
	 * 保存修改的公共代码
	 */
	@RequestMapping(value="saveIndicaotrChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveIndicaotrChange(@ModelAttribute("model") IndicatorModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = indicatorService.saveIndicatorChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 保存新增的公共代码
	 */
	@RequestMapping(value="saveNewIndicator.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewIndicator(@ModelAttribute("model") IndicatorModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = indicatorService.saveNewIndicator(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	/*
	 * 在设备管理中
	 * 为下拉树展示提供数据
	 */
	@RequestMapping(value="getIndicatorList4Tree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorList4Tree(@ModelAttribute("model") DeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		trees = indicatorService.getIndicatorList4Tree(model);
		return JsonUtil.toJson(trees);
	}
	/*
	 * 以树的形式展示当前站点的设备下的有效参数
	 */
	@RequestMapping(value="getIndicators4StationDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicators4StationDevice(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		trees = indicatorService.getIndicators4StationDevice(model);
		return JsonUtil.toJson(trees);
	}
	/*
	 * 以树的形式展示当前站点下的需要计算入海污染量的有效参数
	 */
	@RequestMapping(value="getIndicators4Pollu.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicators4Pollu(HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		trees = indicatorService.getIndicators4Pollu(station);
		return JsonUtil.toJson(trees);
	}
	/*
	 * 获得当前站点的有效参数
	 */
	
	@RequestMapping(value="getIndicatorsByStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorsByStation(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		list = indicatorService.getIndicatorsByStation(model);
		return JsonUtil.toJson(list);
	}
	/*
	 * 获得当前设备下的有效参数
	 */
	@RequestMapping(value="getIndicatorsByDevice.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorsByDevice(@ModelAttribute("model") DeviceModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		list = indicatorService.getIndicators4Deivce(model);
		return JsonUtil.toJson(list);
	}
	
	/*
	 * 根据分组信息查询有效参数列表
	 */
	@RequestMapping(value="getIndicatorsByGroup.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorsByGroup(@ModelAttribute("model") IndicatorGroupModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<IndicatorModel> list = new ArrayList<IndicatorModel>();
		list = indicatorService.getIndicatorsByGroup(model);
		return JsonUtil.toJson(list);
	}	
	/*
	 * 根据功能区得到参数的列表
	 */
	@RequestMapping(value="getIndicatorZtreeList4Domain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicatorZtreeList4Domain(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> list = new ArrayList<ZTreeModel>();
		list = indicatorService.getIndicatorZtreeList4Domain(model);
		return JsonUtil.toJson(list);
	}	
	
	/*
	 * 得到所有有效参数的树的列表
	 * 选中传入参数,
	 * 如果没有传入参数,则选中第一个参数
	 */
	@RequestMapping(value="getAllIndicatorListTree4Single.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getAllIndicatorListTree4Single(@ModelAttribute("model") IndicatorModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		trees = indicatorService.getAllIndicatorListTree4Single(model);
		return JsonUtil.toJson(trees);
	}
	
	/*
	 * 根据站点获得监测的参数列表
	 * 默认选中第一个
	 */
	@RequestMapping(value="getIndicators4StationDevice4Show.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getIndicators4StationDevice4Show(@ModelAttribute("station") StationModel station,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		list = indicatorService.getIndicators4StationDevice4Show(station);
		return JsonUtil.toJson(list);
	}	
}
