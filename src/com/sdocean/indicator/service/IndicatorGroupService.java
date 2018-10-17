package com.sdocean.indicator.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.indicator.dao.IndicatorGroupDao;
import com.sdocean.indicator.model.IndicatorGroupModel;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class IndicatorGroupService {
	
	@Autowired
	private IndicatorGroupDao indicatorGroupDao;
	
	/*
	 * 为公共代码管理提供表头
	 */
	public List<UiColumn> getCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", true, "5%");
		UiColumn col2 = new UiColumn("code", "code", true, "15%");
		UiColumn col3 = new UiColumn("名称", "title", true, "13%");
		UiColumn col4 = new UiColumn("图片链接", "picture", true, "13%");
		UiColumn col5 = new UiColumn("描述", "description", true, "13%");
		UiColumn col6 = new UiColumn("isactive", "isactive", true, "13%");
		UiColumn col18 = new UiColumn("状态", "isactiveName", true, "15%");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col4);
		cols.add(col5);
		cols.add(col18);
		return cols;
	}
	/*
	 * 查询符合条件的参数组
	 */
	public List<IndicatorGroupModel> showIndicatorGroups(IndicatorGroupModel model){
		return indicatorGroupDao.showIndicatorGroups(model);
	}
	
	/*
	 * 保存修改的参数组
	 */
	public Result saveGroupChange(IndicatorGroupModel model){
		return indicatorGroupDao.saveGroupChange(model);
	}
	/*
	 * 保存新增的参数组
	 */
	public Result saveNewGroup(IndicatorGroupModel model){
		return indicatorGroupDao.saveNewGroup(model);
	}
	
	/*
	 * 删除选中的参数组
	 */
	public Result deleGroup(String ids){
		return indicatorGroupDao.deleGroup(ids);
	}
	
}
