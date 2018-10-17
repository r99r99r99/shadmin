package com.sdocean.tool.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.station.dao.StationCommDao;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UeditorService {

	@Resource
	private StationCommDao stationCommDao;
	
	public UeditorModel imageUpload(HttpServletRequest request, HttpServletResponse response,ConfigInfo info){
		String original = "";
		String size = "";
		String fileName = "";
		String url = "";
		//创建一个通用的多部分解析器  
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
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
                    String myFileName = file.getOriginalFilename();  
                    size = file.getSize()+"";
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在  
                    if(myFileName.trim() !=""){ 
                    	original = myFileName;
                        //重命名上传后的文件名  
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        Date now = new Date();
                        String sysPath = info.getFilePath();
                        String filePath = sdf.format(now);
                        int Num=new Random().nextInt(9999)+1;
                        fileName = new Date().getTime() + Num + file.getOriginalFilename();  
                        //定义上传路径  
                        //url = "/images/"+filePath+"/"+fileName; 
                        String path = sysPath+"\\images\\"+filePath+"\\"+fileName; 
                        String fpath = sysPath+"\\images\\"+filePath;
                        url = "/imageShow.do?fileName="+"images\\"+filePath+"\\"+fileName;
                        File fFile = new File(fpath);
                        if(!fFile.exists()){
                        	fFile.mkdirs();
                        }
                        //String path = "E:/"+fileName;
                        File localFile = new File(path);  
                        try {
							file.transferTo(localFile);
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}  
                    }  
                }  
                //记录上传该文件后的时间  
                int finaltime = (int) System.currentTimeMillis();  
            }  
              
        }  
		
		UeditorModel edit = new UeditorModel();
		edit.setOriginal(original);
		edit.setState("SUCCESS");
		edit.setSize(size);
		edit.setTitle(fileName);
		edit.setType(".jpg");
		edit.setUrl(url);
		return edit;
	}
}
