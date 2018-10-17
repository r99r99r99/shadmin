package com.sdocean.frame.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

	public static String getMonthFolderName() {
		return DateUtil.fomatDate(new Date(), "yyyyMM");
	}

	public static String uploadFile(MultipartFile file, String type) throws Exception {
		String filePath = getMonthFolderName() + "/" + type + "/";
		String fileRealPath = Constants.UPLOAD_PATH + filePath;
		File fileDir = new File(fileRealPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs(); // 创建多级目录
		}
		String fileName = file.getOriginalFilename();
		if(StringUtils.isBlank(fileName)){
			return null;
		}
		String time = UUID.randomUUID().toString();
		String fileFullName = fileRealPath + time + "_" + fileName;
		byte[] bytes = file.getBytes();
		FileOutputStream os = new FileOutputStream(fileFullName);
		os.write(bytes);
		return filePath + time + "_" + fileName;
	}

	public static void downloadFile(HttpServletResponse response, String filePath) throws Exception {
		
		if(filePath == null){
			return;
		}
		File downLoadResourceFile = new File(Constants.UPLOAD_PATH + filePath);
		String fileName = downLoadResourceFile.getName();
		fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");

		// 输出文件
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setContentLength(new Long(downLoadResourceFile.length()).intValue());
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		OutputStream out = response.getOutputStream();

		BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
		InputStream in = new FileInputStream(downLoadResourceFile);
		BufferedInputStream bufferedIn = new BufferedInputStream(in);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = bufferedIn.read(buffer)) != -1) {
			bufferedOut.write(buffer, 0, len);
		}
		bufferedIn.close();
		bufferedOut.flush();
		bufferedOut.close();
	}
	
	public static void deleteUploadFile(String filePath) throws Exception {
		if(StringUtils.isBlank(filePath)){
			return;
		}
		delete(Constants.UPLOAD_PATH + filePath);
	}
	
    /**   
     * 删除文件，可以是单个文件或文件夹   
     * @param   fileName    待删除的文件名   
     * @return 文件删除成功返回true,否则返回false   
     */    
    public static boolean delete(String fileName){     
        File file = new File(fileName);     
        if(!file.exists()){     
            System.out.println("删除文件失败："+fileName+"文件不存在");     
            return false;     
        }else{     
            if(file.isFile()){     
                     
                return deleteFile(fileName);     
            }else{     
                return deleteDirectory(fileName);     
            }     
        }     
    }     
         
    /**   
     * 删除单个文件   
     * @param   fileName    被删除文件的文件名   
     * @return 单个文件删除成功返回true,否则返回false   
     */    
    public static boolean deleteFile(String fileName){     
        File file = new File(fileName);     
        if(file.isFile() && file.exists()){     
            file.delete();     
            System.out.println("删除单个文件"+fileName+"成功！");     
            return true;     
        }else{     
            System.out.println("删除单个文件"+fileName+"失败！");     
            return false;     
        }     
    }     
         
    /**   
     * 删除目录（文件夹）以及目录下的文件   
     * @param   dir 被删除目录的文件路径   
     * @return  目录删除成功返回true,否则返回false   
     */    
    public static boolean deleteDirectory(String dir){     
        //如果dir不以文件分隔符结尾，自动添加文件分隔符      
        if(!dir.endsWith(File.separator)){     
            dir = dir+File.separator;     
        }     
        File dirFile = new File(dir);     
        //如果dir对应的文件不存在，或者不是一个目录，则退出      
        if(!dirFile.exists() || !dirFile.isDirectory()){     
            System.out.println("删除目录失败"+dir+"目录不存在！");     
            return false;     
        }     
        boolean flag = true;     
        //删除文件夹下的所有文件(包括子目录)      
        File[] files = dirFile.listFiles();     
        for(int i=0;i<files.length;i++){     
            //删除子文件      
            if(files[i].isFile()){     
                flag = deleteFile(files[i].getAbsolutePath());     
                if(!flag){     
                    break;     
                }     
            }     
            //删除子目录      
            else{     
                flag = deleteDirectory(files[i].getAbsolutePath());     
                if(!flag){     
                    break;     
                }     
            }     
        }     
             
        if(!flag){     
            System.out.println("删除目录失败");     
            return false;     
        }     
             
        //删除当前目录      
        if(dirFile.delete()){     
            System.out.println("删除目录"+dir+"成功！");     
            return true;     
        }else{     
            System.out.println("删除目录"+dir+"失败！");     
            return false;     
        }     
    }

}
