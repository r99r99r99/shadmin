package com.sdocean.main.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.file.action.FileUpload;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.main.model.MainTainFile;
import com.sdocean.main.model.MainTainModel;
import com.sdocean.main.model.MainTenance;
import com.sdocean.main.model.MainTenanceFile;
import com.sdocean.main.service.MainTenanceService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.service.StationService;
import com.sdocean.users.model.SysUser;

@Controller
public class MainTenanceAction {
	@Resource
	MainTenanceService mainSevice;
	@Resource
	OperationLogService logService;
	@Resource
	StationService stationService;
	@Autowired
	private ConfigInfo info;
	
	@RequestMapping("info_mainEdit.do")
	public ModelAndView info_mainedit(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/main/mainEditInfo");
	        return mav;  
	}
	
	@RequestMapping("info_mainShow.do")
	public ModelAndView info_mainShow(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/main/mainShowInfo");
	        return mav;  
	}
	
	/*
	 * 根据用户的权限,查询权限下站点的设备运行状态
	 */
	@RequestMapping(value="showMainEditList.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String showMainEditList(@ModelAttribute("model") MainTainModel model,HttpServletRequest request,
			HttpServletResponse response){
		PageResult page = new PageResult();
		List<UiColumn> cols = mainSevice.getCols4MainEditList();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<StationModel> stations = (List<StationModel>) session.getAttribute("stations");
		List<MainTainModel> rows = mainSevice.getMainList4StationMainEdit(model, stations);
		//返回结果
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	/*
	 * 初始化站点维护上报页面,设置站点列表以及开始结束时间
	 */
	@RequestMapping(value="initMainEditInfo.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String initMainEditInfo(@ModelAttribute("model") MainTenance model,HttpServletRequest request,
			HttpServletResponse response){
		MainTenance result = new MainTenance();
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		List<StationModel> list = stationService.getStations4User(user);
		//初始化上报时间
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    String endDate = beginDf.format(calendar.getTime());
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    result.setStations(list);
	    result.setBeginTime(beginDate);
	    result.setEndTime(endDate);
		return JsonUtil.toJson(result);
	}
	/*
	 * 保存站点设备维护上报记录
	 */
	@RequestMapping(value="saveMainEditInfo.do", method = RequestMethod.POST,produces = "application/text;charset=UTF-8")
	@ResponseBody
	public String saveMainEditInfo(
			@ModelAttribute("model") MainTainModel model,
			HttpServletRequest request,	
			HttpServletResponse response){
		Result result = new Result();
		//得到当前操作人的信息
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		
		//判断该操作是新增还是修改   通过mtype判断
		int mtype = model.getMtype();
		if(mtype==2){//2代表修改
			result = mainSevice.saveChangeMainEdit(model);
		}else{  //mtype=1代表新增
			model.setUserId(user.getId());
			result = mainSevice.saveNewMainEdit(model);
		}
		
		//创建一个通用的多部分解析器  
	     CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
	     List<MainTenanceFile> files = new ArrayList<MainTenanceFile>();
	   //判断 request 是否有文件上传,即多部分请求  
	    if(multipartResolver.isMultipart(request)){  
	    	//转换成多部分request 
	    	MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
	    	//取得request中的所有文件名  
	    	Iterator<String> iter = multiRequest.getFileNames();  
	    	while(iter.hasNext()){  
	    		//记录上传过程起始时的时间，用来计算上传时间  
                int pre = (int) System.currentTimeMillis(); 
              //取得上传文件  
                MultipartFile file = multiRequest.getFile(iter.next());
                if(file != null){ 
                	//取得当前上传文件的文件名称  
                    String fileName = file.getOriginalFilename();  
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(fileName.trim() !=""){  
                    	//重命名上传后的文件名  
                    	FileUpload fileUpload = new FileUpload();
                    	String pathName = fileUpload.saveMainFile(info, "mfile", file);
                    	MainTenanceFile newFile = new MainTenanceFile();
                    	newFile.setRealName(fileName);
                    	newFile.setPathName(pathName);
                    	files.add(newFile);
                    }
                }
	    	}
	    }
	    if(files!=null&&files.size()>0){
	    	MainTainModel main = new MainTainModel();
	    	main.setId((int)result.getRes());
	    	mainSevice.saveFileByMain(main, files);
	    }
		return result.getMessage();
	}
	
	/*
	 * 根据设备维护ID,得到该设备维护记录的图片的列表
	 */
	@RequestMapping(value="getMainFilesByMainId.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getMainFilesByMainId(@ModelAttribute("model") MainTainFile model,HttpServletRequest request,
			HttpServletResponse response){
		List<MainTainFile> files = new ArrayList<>();
		files = mainSevice.getFileListByMain(model);
		return JsonUtil.toJson(files);
	}
	@RequestMapping(value="getPlanMain4FirstPage2.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getPlanMain4FirstPage2(HttpServletRequest request,
			HttpServletResponse response){
		HttpSession session = request.getSession();
		StationModel station = (StationModel) session.getAttribute("station");
		PageResult page = mainSevice.getPlanMain4FirstPage2(station);
		
		return JsonUtil.toJson(page);
	}
	
	/*
	 * 为日常维护提供设备列表
	 */
	@RequestMapping(value="getDeviceIndicatorTree4Main.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getDeviceIndicatorTree4Main(@ModelAttribute("model") MainTainModel model,HttpServletRequest request,
			HttpServletResponse response){
		List<SelectTree> list = new ArrayList<SelectTree>();
		list = mainSevice.getDeviceIndicatorTree4Main(model);
		return JsonUtil.toJson(list);
	}
	
	
	/*
	 * 下载维护文件
	 */
	@RequestMapping(value="/exportMainFile.do")
	public void exportMainFile(
			@RequestParam("fileName") String fileName,
			@RequestParam("filePath") String filePath,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		String path = info.getMainFilePath()+"//"+filePath;
		FileUpload fileUpload = new FileUpload();
		fileUpload.downFile(fileName,path, request, response);
	}	
}
