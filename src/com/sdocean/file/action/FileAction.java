package com.sdocean.file.action;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sdocean.common.model.Result;
import com.sdocean.file.model.SysFile;
import com.sdocean.file.service.FileService;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.service.OperationLogService;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;

@Controller
public class FileAction {

	private static Logger log = Logger.getLogger(FileAction.class);  
	@Autowired
	private FileService fileSerivce;
	@Resource
	OperationLogService logService;
	@Autowired
	private ConfigInfo info;
	
	
	@RequestMapping("info_file.do")
	public ModelAndView info_file(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		    ModelAndView mav = new ModelAndView("/"+info.getPageVision()+"/file/fileInfo");
	        return mav;  
	}
	/*
	 * 初始化文件列表查询页面
	 */
	@RequestMapping(value="/init_file.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String init_file(HttpServletRequest request,HttpServletResponse response){
		SysFile model = new SysFile();
		DateFormat beginDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    
	    //设置结束时间
	    String endDate = beginDf.format(calendar.getTime());
	    //设置开始时间
	    calendar.add(Calendar.MONTH, -1);
	    String beginDate = beginDf.format(calendar.getTime());
	    model.setBeginTime(beginDate);
	    model.setEndTime(endDate);
		return JsonUtil.toJson(model);
	}
	@RequestMapping(value="/saveFileUpload.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveFileUpload(@RequestParam MultipartFile file,
			@RequestParam("filepath") String filepath,
			HttpServletRequest request,HttpServletResponse response){
		//保存文件,并获得随机名称
		FileUpload fileupload = new FileUpload();
		String realName;
		try {
			realName = fileupload.saveFileUpload(info,filepath, file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "保存失败";
		}
		//初始化保存条件
		SysFile sFile = new SysFile();
		//获得文件的原来名称
		sFile.setFileName(file.getOriginalFilename());
		//获得文件的系统随机名称
		sFile.setRealName(realName);
		//设置文件的存储类型 1:文件存在系统内部,2:文件存在系统外部
		sFile.setType(1);
		//获得文件的相对路径
		sFile.setRelativePath(filepath);
		//获得文件的绝对路径
		sFile.setAbsolutelyPath(info.getSysPath()+"/"+filepath);
		
		Result result = fileSerivce.saveFile(sFile);
		//
		return result.getMessage();
	}
	/*
	 * 根据条件展示文件列表
	 */
	@RequestMapping(value="/showFileList.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public String showFileList(@ModelAttribute("model") SysFile model,
			HttpServletRequest request,HttpServletResponse response){
		//获得http路径
		String httpPath = request.getRequestURL().toString();
		PageResult page = new PageResult();
		//获得展示列表的表头
		List<UiColumn> cols = fileSerivce.getCols4List();
		//获得展示列表的结果
		List<SysFile> rows = fileSerivce.getFileList(httpPath, model);
		page.setCols(cols);
		page.setRows(rows);
		return JsonUtil.toJson(page);
	}
	
	/*
	 * 根据绝对路径获得图片
	 */
	@RequestMapping(value="/getPicture.do")
	public void getPicture(HttpServletRequest request,HttpServletResponse response){
		   String path = request.getParameter("Path");
		   response.setHeader("Pragma","No-cache");    
		   response.setHeader("Cache-Control","no-cache");    
		   response.setDateHeader("Expires", 0);    
		   
		   BufferedInputStream bis = null;  
		   OutputStream os = null;  
		   FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		   
		   bis = new BufferedInputStream(fileInputStream);  
		   byte[] buffer = new byte[512];  
		   response.reset();  
		   response.setCharacterEncoding("UTF-8");  
		   //不同类型的文件对应不同的MIME类型  
		   response.setContentType("image/png");  
		   //文件以流的方式发送到客户端浏览器  
		   //response.setHeader("Content-Disposition","attachment; filename=img.jpg");  
		   //response.setHeader("Content-Disposition", "inline; filename=img.jpg");  
	    try {
			response.setContentLength(bis.available());
			os = response.getOutputStream();  
			int n;  
		    while ((n = bis.read(buffer)) != -1) {  
			     os.write(buffer, 0, n);  
			}
			bis.close();  
			os.flush();  
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		   
	}
}
