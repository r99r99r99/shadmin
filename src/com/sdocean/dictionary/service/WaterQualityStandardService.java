package com.sdocean.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.PlotLine;
import com.sdocean.common.model.Result;
import com.sdocean.dictionary.dao.WaterQualityStandardDao;
import com.sdocean.dictionary.model.WaterQualityStandardModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class WaterQualityStandardService {

	@Resource
	private WaterQualityStandardDao standardDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4GroupList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("编码", "item", false, "*");
		UiColumn col21 = new UiColumn("编码", "indicatorName", true, "*");
		UiColumn col3 = new UiColumn("standardId", "standardId", true, "*");
		UiColumn col5 = new UiColumn("等级名称", "standardName", true, "*");
		UiColumn col4 = new UiColumn("下限", "min_value", true, "*");
		UiColumn col14 = new UiColumn("下限计算方式", "minKey", true, "*");
		UiColumn col15 = new UiColumn("min", "min", false, "*");
		UiColumn col6 = new UiColumn("上限", "max_value", true, "*");
		UiColumn col24 = new UiColumn("上限计算方式", "maxKey", true, "*");
		UiColumn col25 = new UiColumn("max", "max", false, "*");
		UiColumn col18 = new UiColumn("备注", "remarks", true, "*");
		UiColumn col19 = new UiColumn("水质类型名称", "waterTypeName", true, "*");
		UiColumn col20 = new UiColumn("waterType", "waterType", false, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col21);
		cols.add(col3);
		cols.add(col5);
		cols.add(col6);
		cols.add(col24);
		cols.add(col25);
		cols.add(col4);
		cols.add(col14);
		cols.add(col15);
		cols.add(col18);
		cols.add(col19);
		cols.add(col20);
		return cols;
	}
	

	/*
	 * 得到所有的水质标准的列表
	 */
	public List<WaterQualityStandardModel> getStandardList(WaterQualityStandardModel model){
		return standardDao.getStandardList(model);
	}
	
	/*
	 * 保存水质标准的修改
	 */
	public Result saveQualityChange(WaterQualityStandardModel model){
		return standardDao.saveQualityChange(model);
	}
	
	/*
	 * 保存水质标准的修改
	 */
	public Result saveNewQuality(WaterQualityStandardModel model){
		return standardDao.saveNewQuality(model);
	}
	
	/*
	 * 删除选中的水质等级
	 */
	public Result deleWaterQulity(WaterQualityStandardModel model){
		return standardDao.deleWaterQulity(model);
	}
	/*
	 * 通过站点以及参数获得该参数的水质标准线
	 */
	public List<PlotLine> getPlotLines(StationModel station,String code){
		return standardDao.getPlotLines(station, code);
	}
}
