package com.sdocean.file.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.sdocean.file.model.FileModel;
import com.sdocean.frame.model.ConfigInfo;

public class FileUpload {
	
	/*
	 * 根据路径,把文件存入到路径下,
	 * 并返回新生成的文件名称
	 */
	public String saveFileUpload(ConfigInfo info,String filePath,MultipartFile file) throws Exception{
		int pre = (int) System.currentTimeMillis();
		FileOutputStream os = null;
		FileInputStream in = null;
		//定义新文件名称
		String fileName = new Date().getTime() + file.getOriginalFilename();
		
		//try {
			os = new FileOutputStream(info.getSysPath()+"/"+ filePath +"/"+ fileName);
			in = (FileInputStream) file.getInputStream();
			 //以写字节的方式写文件  
            int b = 0;  
            while((b=in.read()) != -1){  
                os.write(b);  
            }  
            os.flush();  
            
            int finaltime = (int) System.currentTimeMillis();  
		//} catch (Exception  e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}finally{
			try {
				os.close();
				in.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		//}
		
		return fileName;
	}
	
	/*
	 * 根据路径,吧文件存入路径下,
	 * 并返回新生成的文件名称
	 */
	public String saveFile(ConfigInfo info,String filePath,MultipartFile file){
		String fpath = info.getSysPath()+"/"+filePath;
		File fpFile = new File(fpath);
		if(!fpFile.exists()) {
			fpFile.mkdirs();
		}
		//定义新文件名称
		String fileName = new Date().getTime() + file.getOriginalFilename();
         //定义上传路径  
           String path = info.getSysPath()+"/"+filePath+"/" + fileName;  
           File localFile = new File(path);  
           try {
				file.transferTo(localFile);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
           
        fileName =   filePath+"/" + fileName;
		return fileName;
	}
	
	/*
	 * 根据文件类别保存文件,并获得新文件名
	 */
	public FileModel saveFile(ConfigInfo info,String configName,String filePath,MultipartFile file){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//定义新文件名称
		String fileName = new Date().getTime() + file.getOriginalFilename();
		FileModel  fm = new FileModel();
		fm.setStatus(true);
		//处理config中的方法, 将方法中的第一个字符变为大写
		configName = "get"+configName.substring(0, 1).toUpperCase()+ configName.substring(1, configName.length());
		
		
		//定义新文件名称
		String newName = new Date().getTime() + file.getOriginalFilename();
        
		try {
			Method getType = info.getClass().getMethod(configName);
			String typeName = (String) getType.invoke(info);
			fm.setConfigName(typeName);
			String fpath = "";
			if(typeName.substring(typeName.length()-1, typeName.length()).equals("/")||typeName.substring(typeName.length()-1, typeName.length()).equals("\\")){
				fpath = typeName+filePath;
			}else{
				fpath = typeName+"/"+filePath;
			}
			File fpFile = new File(fpath);
			if  (fpFile .exists())      
			{       
				
			}else{
				//System.out.println("//不存在");  
			    fpFile .mkdir();  
			}
			
			String path = typeName+"/"+filePath+"/" + fileName;  
			File localFile = new File(path);  
			file.transferTo(localFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fm.setStatus(false);
			fm.setMessage("上传失败");
			e.printStackTrace();
		} 
		fm.setBeforeName(file.getOriginalFilename());
		fm.setAfterName(newName);
		fm.setFilePath(filePath);
		fm.setCreateTime(sdf.format(new Date()));
		
		return fm;
	}
	/*
	 * 保存站点维护记录
	 */
	public String saveMainFile(ConfigInfo info,String filePath,MultipartFile file){
		//定义新文件名称
		String fileName = new Date().getTime() + file.getOriginalFilename();
		String fpath = info.getMainFilePath()+"/"+filePath;
		File fpFile = new File(fpath);
		if(!fpFile.exists()) {
			fpFile.mkdirs();
		}
         //定义上传路径  
           String path = info.getMainFilePath()+"/"+filePath+"/" + fileName;  
           File localFile = new File(path);  
           try {
				file.transferTo(localFile);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
           
        fileName =   filePath+"/" + fileName;
		return fileName;
	}
	
	/*
	 * 下载文件
	 */
	public void downFile(String fileName,String filePath,
			HttpServletRequest request,HttpServletResponse response) throws Exception{
		//String name = "graphquery_init.js";
		//String path = "E:\\mainFile\\mfile\\"+"1521684025366graphquery_init.js";
		//声明本次下载状态的记录对象
	    //设置响应头和客户端保存文件名
	    response.setCharacterEncoding("utf-8");
	    response.setContentType("multipart/form-data");
	    response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
	    //用于记录以完成的下载的数据量，单位是byte
	    long downloadedLength = 0l;
	    try {
	        //打开本地文件流
	        InputStream inputStream = new FileInputStream(filePath);
	        //激活下载操作
	        OutputStream os = response.getOutputStream();

	        //循环写入输出流
	        byte[] b = new byte[2048];
	        int length;
	        while ((length = inputStream.read(b)) > 0) {
	            os.write(b, 0, length);
	            downloadedLength += b.length;
	        }

	        // 这里主要关闭。
	        os.close();
	        inputStream.close();
	    } catch (Exception e){
	        throw e;
	    }
	}
	
	 /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
}	
