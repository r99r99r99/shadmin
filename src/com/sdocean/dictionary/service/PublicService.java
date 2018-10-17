package com.sdocean.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dictionary.dao.PublicDao;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class PublicService {
	
	@Autowired
	private PublicDao publicDao;
	
	public List<PublicModel> getPublicsByParent(String parentCode){
		return publicDao.getPublicsByParent(parentCode);
	}
	/*
	 * 为公共代码管理提供表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("parentCode", "parentCode", true, "*");
		UiColumn col3 = new UiColumn("classId", "classId", true, "*");
		UiColumn col4 = new UiColumn("classCode", "classCode", true, "*");
		UiColumn col5 = new UiColumn("className", "className", true, "*");
		UiColumn col6 = new UiColumn("value", "value", true, "*");
		UiColumn col18 = new UiColumn("remark", "remark", true, "*");
		UiColumn col19 = new UiColumn("orderCode", "orderCode", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col4);
		cols.add(col5);
		cols.add(col18);
		cols.add(col19);
		return cols;
	}
	
	/*
	 * 查询符合条件的公共代码
	 */
	public List<PublicModel> getPublics(PublicModel model){
		return publicDao.getPublics(model);
	}
	
	/*
	 * 保存修改的公共代码
	 */
	public Result savePublicChange(PublicModel model){
		return publicDao.savePublicChange(model);
	}
	
	/*
	 * 保存新增的公共代码
	 */
	public Result saveNewPublic(PublicModel model){
		return publicDao.saveNewPublic(model);
	}
	

	/*
	 * 删除选中的公共代码
	 */
	public Result delePublic(String ids){
		return publicDao.delePublic(ids);
	}
}
