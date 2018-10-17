package com.sdocean.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dictionary.dao.UnitGroupDao;
import com.sdocean.dictionary.model.UnitGroupModel;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UnitGroupService {

	@Resource
	private UnitGroupDao unitGroupDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4GroupList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("名称", "name", true, "*");
		UiColumn col6 = new UiColumn("备注", "description", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		return cols;
	}
	
	/*
	 * 得到所有的单位组的列表
	 */
	public List<UnitGroupModel> getUnitGroups(UnitGroupModel model){
		return unitGroupDao.getUnitGroups(model);
	}
	
	/*
	 * 保存修改信息
	 */
	public Result saveGroupChange(UnitGroupModel model){
		return unitGroupDao.saveGroupChange(model);
	}
	
	/*
	 * 保存单位组的创建
	 */
	public Result saveNewGroup(UnitGroupModel model){
		return unitGroupDao.saveNewGroup(model);
	}
	
	/*
	 * 获得有效的单位组的集合
	 */
	public List<UnitGroupModel> getUnitGroups(){
		return unitGroupDao.getUnitGroups();
	}
	
}
