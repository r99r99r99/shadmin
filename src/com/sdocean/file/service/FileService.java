package com.sdocean.file.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.file.dao.FileDao;
import com.sdocean.file.model.SysFile;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class FileService {
	
	@Autowired
	private FileDao fileDao;
	
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("原名", "fileName", true, "*");
		UiColumn col3 = new UiColumn("保存后的名称", "realName", true, "*");
		UiColumn col4 = new UiColumn("绝对路径","absolutelyPath", true, "*");
		UiColumn col5 = new UiColumn("相对路径", "relativePath", true, "*");
		UiColumn col6 = new UiColumn("创建时间", "createtime", true, "*");
		UiColumn col7 = new UiColumn("网络路径", "httpPath", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		return cols;
	}
	/*
	 * 展示系统的图片信息
	 */
	public List<SysFile> getFileList(String httpPath,SysFile model){
		return fileDao.getFileList(httpPath, model);
	}
	/*
	 * 保存图片信息
	 */
	public Result saveFile(SysFile file){
		return fileDao.saveFile(file);
	}
	
}
