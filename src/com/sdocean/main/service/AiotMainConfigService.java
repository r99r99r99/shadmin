package com.sdocean.main.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.main.dao.AiotMainConfigDao;
import com.sdocean.main.model.AiotMainConfigModel;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class AiotMainConfigService {
	
	@Autowired
	AiotMainConfigDao aiotMainConfigDao;
	public List<UiColumn> getCols4MainEditList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("配置名称", "name", true, "*");
		UiColumn col4 = new UiColumn("操作步骤", "how", true, "*");
		UiColumn col6 = new UiColumn("排序编码", "orderCode", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col6);
		return cols;
	}
	/*
	 * 获得例行维护分类列表
	 */
	public List<AiotMainConfigModel> getAiotMainConfigList(AiotMainConfigModel model){
		return aiotMainConfigDao.getAiotMainConfigList(model);
	}
	/*
	 * 新增例行维护分类
	 */
	public Result saveNewMainConfig(AiotMainConfigModel model){
		return aiotMainConfigDao.saveNewMainConfig(model);
	}
	/*
	 * 修改例行维护分类
	 */
	public Result saveChangeMainConfig(AiotMainConfigModel model){
		return aiotMainConfigDao.saveChangeMainConfig(model);
	}
	/*
	 *  删除例行维护分类
	 */
	public Result deleMainConfig(AiotMainConfigModel model){
		return aiotMainConfigDao.deleMainConfig(model);
	}
}
