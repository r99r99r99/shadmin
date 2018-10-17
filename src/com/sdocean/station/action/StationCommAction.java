package com.sdocean.station.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.file.action.FileUpload;
import com.sdocean.file.model.FileModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.main.model.MainTenanceFile;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationDeviceComm;
import com.sdocean.station.model.StationInfo;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationPictureModel;
import com.sdocean.station.service.StationCommService;
import com.sdocean.users.model.SysUser;

@Controller
public class StationCommAction {
	@Resource
	StationCommService stationCommService;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_stationComm.do")
	public ModelAndView info_mainedit(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationCommInfo");
	        return mav;  
	}
	/*
	 * 跳转到站点信息编辑页面
	 */
	@RequestMapping("info_stationInfo.do")
	public ModelAndView info_stationInfo(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationInfo_init");
	        return mav;  
	}
	/*
	 * 跳转到站点信息展示页面
	 */
	@RequestMapping("info_stationShow.do")
	public ModelAndView info_stationShow(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/station/stationShow_init");
		    HttpSession session = request.getSession();
		    StationModel station = (StationModel) session.getAttribute("station");
		    StationInfo model = new StationInfo();
		    model.setStationId(station.getId());
		    
		    
		    StationInfo result = new StationInfo();
			result = stationCommService.getStationInfoByWpId(model);
			mav.addObject("infomation", result.getInfomation());
	        return mav;  
	}
	/*
	 * 跳转到站点设备展示页面
	 */
	@RequestMapping("info_stationDeviceShow.do")
	public ModelAndView info_stationDeviceShow(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/station/stationDeviceShow_init");
		  /*  HttpSession session = request.getSession();
		    StationModel station = (StationModel) session.getAttribute("station");
		    StationInfo model = new StationInfo();
		    model.setStationId(station.getId());
		    
		    
		    StationInfo result = new StationInfo();
			result = stationCommService.getStationInfoByWpId(model);
			mav.addObject("infomation", result.getInfomation());*/
	        return mav;  
	}
	
	/*
	 * 展示站点维护列表
	 */
	@RequestMapping(value="showStationCommList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showStationCommList(@ModelAttribute("model") StationDeviceComm model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		//得到展示表格的表头
		List<UiColumn> cols = stationCommService.getCols4TypeList();
		//得到展示表格的结果集
		List<StationDeviceComm> rows = stationCommService.getStationCommList(model);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 保存修改
	 */
	@RequestMapping(value="saveStationCommChange.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveStationCommChange(@ModelAttribute("model") StationDeviceComm model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = stationCommService.saveStationCommChange(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 保存新增
	 */
	@RequestMapping(value="saveNewStationComm.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveNewStationComm(@ModelAttribute("model") StationDeviceComm model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = stationCommService.saveNewStationComm(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	/*
	 * 得到该站点下的设备列表,并选中已经查询的设备
	 */
	@RequestMapping(value="getDevciesByStation4Tree.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDevciesByStation4Tree(@ModelAttribute("model") StationModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> trees = new ArrayList<SelectTree>();
		trees = stationCommService.getDevciesByStation4Tree(model);
		return JsonUtil.toJson(trees);
	}
	
	
	/*
	 * 初始化站点信息页面
	 */
	@RequestMapping(value="init_getStationInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String init_getStationInfo(@ModelAttribute("model") StationInfo model,HttpServletRequest request,
			HttpServletResponse response){
		StationInfo result = new StationInfo();
		result = stationCommService.getStationInfoByWpId(model);
		return JsonUtil.toJson(result);
	}
	/*
	 * 保存,更新站点信息
	 */
	@RequestMapping(value="saveStationInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveStationInfo(@ModelAttribute("model") StationInfo model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = stationCommService.saveStationInfomation(model);
		//保存操作信息
		logService.saveOperationLog(result, request);
		
		return result.getMessage();
	}
	
	/*
	 * 删除站点信息
	 */
	@RequestMapping(value="deleStationComm.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleStationComm(@ModelAttribute("model") StationDeviceComm model,HttpServletRequest request,
			HttpServletResponse response){
		Result result = new Result();
		result = stationCommService.deleStationComm(model);
		//保存操作记录
		logService.saveOperationLog(result, request);
		return result.getMessage();
	}
	
	/*
	 * 跳转到站点图片信息编辑页面
	 */
	@RequestMapping("info_stationPicture.do")
	public ModelAndView info_stationPicture(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/station/stationPicture_info");
	        return mav;  
	}
	
	/*
	 * 获取站点图片列表
	 */
	@RequestMapping(value="getStationPicListByStationType.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationPicListByStationType(@ModelAttribute("model") StationPictureModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<StationPictureModel> result =  stationCommService.getStationPicListByStationType(info,model);
		return JsonUtil.toJson(result);
	}
	
	@RequestMapping(value="saveStationPics.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String saveStationPics(@ModelAttribute("model") StationPictureModel stationPictureModel,
						HttpServletRequest request,@RequestParam MultipartFile[] commoPicArr){
		List<FileModel> fms = new ArrayList<>();
		//获得用户的操作信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		stationPictureModel.setUserId(user.getId());
		 String fileName = "";
		 Map<String, Object> json = new HashMap<>();
		 FileModel  fm  = new FileModel();
		 String fname = "";
		 for(MultipartFile file:commoPicArr){
			 fileName = file.getOriginalFilename(); 
           //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
             if(fileName.trim() !=""){  
             	//重命名上传后的文件名  
             	FileUpload fileUpload = new FileUpload();
             	fm  = fileUpload.saveFile(info,"filePath", info.getStationPicPath(), file);
             	if(fm.getStatus()==false){
             		json.put("message", "图片保存失败");
            	    json.put("status", false);
            	    return JsonUtil.toJson(json);
             	}
             	fms.add(fm);
             }
		 }
		Result result = stationCommService.saveStationPic(stationPictureModel, fms);
		Boolean status = true;
		if(result.getResult()==0){
			status = false;
		}
	    json.put("message", result.getMessage());
	    json.put("status", status);
	    return JsonUtil.toJson(json);
	}
	
	/*
	 * 根据ID获得站点图片信息的具体信息
	 */
	@RequestMapping(value="getStationPicModelById.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getStationPicModelById(@ModelAttribute("model") StationPictureModel model,HttpServletRequest request,
			HttpServletResponse response){
		StationPictureModel result =  stationCommService.getStationPicModelById(info,model.getId());
		return JsonUtil.toJson(result);
	}
	
	
	/*
	 * 删除文件
	 */
	/*
	 * 根据ID获得站点图片信息的具体信息
	 */
	@RequestMapping(value="deleStationPic.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String deleStationPic(@ModelAttribute("model") StationPictureModel model,HttpServletRequest request,
			HttpServletResponse response){
		//获得图片所在的imagePath 路径
		String imagePath = info.getFilePath();
		if(imagePath.substring(imagePath.length()-1, imagePath.length()).equals("//")
				||imagePath.substring(imagePath.length()-1, imagePath.length()).equals("/\\") ){
			
		}else{
			imagePath = imagePath + "//";
		}
		//获得图片所在的stationPicPath 路径
		String stationPicPath = info.getStationPicPath();
		String filePath = imagePath + stationPicPath;
		if(filePath.substring(filePath.length()-1, filePath.length()).equals("//")
				||filePath.substring(filePath.length()-1, filePath.length()).equals("/\\")){
			
		}else{
			filePath = filePath + "//";
		}
		String fileName = filePath + model.getModiName();
		
		Boolean status = true;
		
		Result result =  stationCommService.deleStationPic(model);
		if(result.getResult()==1){
			FileUpload fileUpload = new FileUpload();
			status = fileUpload.deleteFile(fileName);
			if(status==false){
				result.setMessage("删除图片文件时失败");
			}
		}
		return result.getMessage();
	}
	
}
