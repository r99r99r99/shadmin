package com.sdocean.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.dictionary.dao.UnitDao;
import com.sdocean.dictionary.model.UnitModel;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class UnitService {

	@Resource
	private UnitDao unitDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4GroupList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("code", "code", true, "*");
		UiColumn col3 = new UiColumn("名称", "name", true, "*");
		UiColumn col5 = new UiColumn("单位组", "groupId", true, "*");
		UiColumn col4 = new UiColumn("单位组", "groupName", true, "*");
		UiColumn col6 = new UiColumn("备注", "description", true, "*");
		UiColumn col7 = new UiColumn("logo", "logo", true, "*");
		UiColumn col20 = new UiColumn("标准编码", "standardCode", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col4);
		cols.add(col5);
		cols.add(col20);
		return cols;
	}
	
	/*
	 * 得到所有的单位组的列表
	 */
	public List<UnitModel> getUnitList(UnitModel model){
		return unitDao.getUnitList(model);
	}
	
	/*
	 * 保存修改信息
	 */
	public Result saveUnitChange(UnitModel model){
		return unitDao.saveUnitChange(model);
	}
	
	/*
	 * 保存单位组的创建
	 */
	public Result saveNewUnit(UnitModel model){
		return unitDao.saveNewUnit(model);
	}
	/*
	 * 以树的形式展示单位列表
	 */
	public List<SelectTree> getUnitList4Tree(UnitModel model){
		return unitDao.getUnitList4Tree(model);
	}
}
