package com.sdocean.tool.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.ueditor.ActionEnter;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.tool.service.UeditorModel;
import com.sdocean.tool.service.UeditorService;

@Controller
public class UeditorAction {
	@Resource
	UeditorService ueditorService;
	@Resource
	ConfigInfo info;
	
	@RequestMapping("ueditorControll.do")
	public void ueditorControll(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		
		Map map = new HashMap();  
	     Enumeration paramNames = request.getParameterNames();  
	    while (paramNames.hasMoreElements()) {  
	      String paramName = (String) paramNames.nextElement();  
	      String[] paramValues = request.getParameterValues(paramName);  
	      if (paramValues.length == 1) {  
	        String paramValue = paramValues[0];  
	        if (paramValue.length() != 0) {  
	        }  
	      }  
	    }  
		request.setCharacterEncoding( "utf-8" );
		response.setHeader("Content-Type" , "text/html");
		String rootPath = getClass().getResource("/").getFile().toString(); 
		response.getWriter().write( new ActionEnter( request, rootPath ).exec() );
	}
	@RequestMapping(value="imageUpload.do", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String imageUpload(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		UeditorModel edit =ueditorService.imageUpload(request, response,info);
		return JsonUtil.toJson(edit);
	}
	@RequestMapping("imageShow.do")
	public void imageShow(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		String fileName = (String) request.getParameter("fileName");
		String path = info.getFilePath()+"/"+fileName;
		File file = new File(path);
	    BufferedImage image = ImageIO.read(file);
	    ImageIO.write(image, "jpg", response.getOutputStream());
	}
}
