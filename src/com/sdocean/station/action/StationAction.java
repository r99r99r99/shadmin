package com.sdocean.station.action;

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
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.common.service.ZTreeService;
import com.sdocean.company.model.CompanyModel;
import com.sdocean.company.service.CompanyService;
import com.sdocean.domain.model.DomainModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.NgColumn;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.region.model.RegionModel;
import com.sdocean.region.service.RegionService;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;

@Controller
public class StationAction {
	@Resource
	StationService stationService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ZTreeService ztreeService;
	@Autowired
	private RegionService regionService;
	
	@RequestMapping("info_station.do")
	public ModelAndView info_station(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationInfo");
	        return mav;  
	}
	
	@RequestMapping(value="showStationList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showStationTypes(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = stationService.getCols4TypeList();
		result.setCols(cols);
		//为查询结果增加列表
		List<StationModel> rows = stationService.getStationList(model);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 保存修改站点信息
	 */
	@RequestMapping(value="saveStaionChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveStaionChange(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = stationService.saveStaionChange(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 保存新增站点信息
	 */
	@RequestMapping(value="saveNewStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewStation(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = stationService.saveNewStation(model);
		logService.saveOperationLog(result,request);
		
		return result.getMessage();
	}
	
	/*
	 * 获得ZTREE展示列表
	 */
	@RequestMapping(value="getStationList4ZTree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationList4ZTree(@ModelAttribute("role") RoleModel role,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> result = new ArrayList<ZTreeModel>();
		result = stationService.getStationListByRole(role);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 获得session中保存的station
	 */
	@RequestMapping(value="getStation.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStation(HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		
		return JsonUtil.toJson(station);
	}
	
	/*
	 * 获得当前用户权限下的站点列表
	 */
	@RequestMapping(value="getStationByUser.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationByUser(HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<StationModel> list = stationService.getStations4User(user);
		return JsonUtil.toJson(list);
	}
	
	@RequestMapping(value="showStationList4Sms.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showStationList4Sms(HttpServletRequest request,
			HttpServletResponse response){
		PageResult result = new PageResult();
		//为查询结果增加表头
		List<UiColumn> cols = stationService.getStationCols4Sms();
		result.setCols(cols);
		//为查询结果增加列表
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<StationModel> rows = stationService.getStations4User(user);
		result.setRows(rows);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 获得组织机构的站点列表
	 */
	@RequestMapping(value="getStation4ComanyZtreeByUser.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStation4ComanyZtreeByUser(HttpServletRequest request,
			HttpServletResponse response,ZTreeModel ztree){
		String syspath = "http://"+request.getHeader("host")+request.getContextPath();
		List<ZTreeModel> ztrees = new ArrayList<ZTreeModel>();
		//获得用户权限下的站点的列表
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<StationModel> stations = stationService.getStations4User(user);
		//根据站点列表获得对应的组织机构列表
		List<CompanyModel> comps = new ArrayList<CompanyModel>();
		List<ZTreeModel> comztrees = new ArrayList<ZTreeModel>();
		List<ZTreeModel> stationztrees = new ArrayList<ZTreeModel>();
		for(StationModel station:stations){
			comps.addAll(companyService.getComListByCode(station.getCompanyId()));
			station.setUrl(ztree.getUrl()+station.getId());
		}
		List<CompanyModel> coms = new ArrayList<CompanyModel>();
		//将重复的组织机构去掉
		for(CompanyModel com:comps){
			if(!coms.contains(com)){
				coms.add(com);
			}
		}
		//将组织机构列表以及站点列表转换成ztree的形式
		try {
			comztrees = ztreeService.changeModel2ZTree(coms, "code", "pcode", "shortName",true,"","","",syspath);
			stationztrees = ztreeService.changeModel2ZTree(stations, "id", "companyId", "title",true,"url",ZTreeModel.getSelf(),"icon",syspath);
			ztrees.addAll(comztrees);
			ztrees.addAll(stationztrees);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JsonUtil.toJson(ztrees);
	}
	
	/*
	 * 获得地区树下的站点列表
	 */
	@RequestMapping(value="getStation4RegionZtreeByUser.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStation4RegionZtreeByUser(HttpServletRequest request,
			HttpServletResponse response,ZTreeModel ztree){
		String syspath = "http://"+request.getHeader("host")+request.getContextPath();
		List<ZTreeModel> ztrees = new ArrayList<ZTreeModel>();
		//获得用户权限下的站点的列表
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<StationModel> stations = stationService.getStations4User(user);
		//根据站点列表得到对应的地区列表
		List<RegionModel> regions = new ArrayList<>();
		List<RegionModel> regs = new ArrayList<>();
		for(StationModel station:stations){
			regions.addAll(regionService.getRegionListById(station.getRegion_id()));
			station.setUrl(ztree.getUrl()+station.getId());
			List<ZTreeModel> devices = stationService.getDeviceZTree4Station(station);
		}
		for(RegionModel region:regions){
			if(!regs.contains(region)){
				regs.add(region);
			}
		}
		//将地区机构列表以及站点列表转换成ZTREE的形式
		List<ZTreeModel> regztrees = new ArrayList<ZTreeModel>();
		List<ZTreeModel> stationztrees = new ArrayList<ZTreeModel>();
		
		try {
			regztrees = ztreeService.changeModel2ZTree(regs, "id", "pid", "text",true,"","","",syspath);
			stationztrees = ztreeService.changeModel2ZTree(stations, "id", "region_id", "title",true,"url",ZTreeModel.getSelf(),"icon",syspath);
			ztrees.addAll(regztrees);
			ztrees.addAll(stationztrees);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return JsonUtil.toJson(ztrees);
	}
	
	
	/*
	 * 获得ZTREE展示列表
	 */
	@RequestMapping(value="getStationTreeByDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationTreeByDomain(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<ZTreeModel> result = new ArrayList<ZTreeModel>();
		result = stationService.getStationTreeByDomain(model);
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 根据站点ID获得站点信息
	 */
	@RequestMapping(value="getStationById.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationById(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		StationModel result = stationService.getStationById(model.getId());
		return JsonUtil.toJson(result);
	}
	
	/*
	 * 得到当前功能区下的站点列表
	 */
	@RequestMapping(value="getStationListByDomain.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationListByDomain(@ModelAttribute("model") DomainModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<StationModel> result = new ArrayList<StationModel>();
		result = stationService.getStationListByDomain(model);
		return JsonUtil.toJson(result);
	}
}
