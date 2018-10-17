package com.sdocean.frame.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public  class ConfigInfo {
	
	@Value("#{configProperties['systemCode']}")
	private String systemCode;
	
	@Value("#{configProperties['systemName']}")
	private  String systemName;
	
	@Value("#{configProperties['sysPath']}")
	private String sysPath;
	
	@Value("#{configProperties['pageVision']}")
	private String pageVision;
	
	@Value("#{configProperties['mainFilePath']}")
	private String mainFilePath;
	
	@Value("#{configProperties['stationPicPath']}")
	private String stationPicPath;
	
	@Value("#{configProperties['filePath']}")
	private String filePath;  //系统的文件附件所放的位置
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getStationPicPath() {
		return stationPicPath;
	}

	public void setStationPicPath(String stationPicPath) {
		this.stationPicPath = stationPicPath;
	}

	public String getMainFilePath() {
		return mainFilePath;
	}

	public void setMainFilePath(String mainFilePath) {
		this.mainFilePath = mainFilePath;
	}

	public String getPageVision() {
		return pageVision;
	}

	public void setPageVision(String pageVision) {
		this.pageVision = pageVision;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSysPath() {
		return sysPath;
	}

	public void setSysPath(String sysPath) {
		this.sysPath = sysPath;
	}
	
	
	/*public ConfigInfo() {
		super();
		Properties props = new Properties();
		InputStream in = getClass().getResourceAsStream("config.properties");
		//InputStream in = ClassLoader.getSystemResourceAsStream("config.properties");
		try {
			props.load(in);
			Enumeration en = props.propertyNames();
            while (en.hasMoreElements()) {
             String key = (String) en.nextElement();
             String Property = props.getProperty (key);
                   if(key.equals("systemCode")){
                	   this.setSystemCode(Property);
                   }else if(key.equals("systemName")){
                	   this.setSystemName(Property);
                   }
               }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	


	
}
