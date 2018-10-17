package com.sdocean.warn.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;
import com.sdocean.warn.dao.RangeDataDao;
import com.sdocean.warn.dao.WarnDao;
import com.sdocean.warn.model.RangeDataModel;
import com.sdocean.warn.model.Warn4FirstModel;
import com.sdocean.warn.model.WarnModel;
import com.sdocean.warn.model.WarnValueModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class RangeDataService {

	@Resource
	RangeDataDao rangeDataDao;

	public List<UiColumn> getCols4RangeDataList() {
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点", "stationName", true, "*");
		UiColumn col4 = new UiColumn("deviceId", "deviceId", false, "*");
		UiColumn col5 = new UiColumn("设备", "deviceName", true, "*");
		UiColumn col6 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col7 = new UiColumn("参数", "indicatorName", true, "*");
		UiColumn col8 = new UiColumn("量程下限", "minData", true, "*");
		UiColumn col9 = new UiColumn("量程上限", "maxData", true, "*");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		return cols;
	}
	/**
	 * 得到查询条件下的站点的错误数据配置
	 */
	public List<RangeDataModel> getRangeDataList(RangeDataModel model){
		return rangeDataDao.getRangeDataList(model);
	}
	
	/*
	 * 新增错误数据配置
	 */
	public Result saveNewRangeData(RangeDataModel model){
		return rangeDataDao.saveNewRangeData(model);
	}
	/*
	 * 修改错误数据配置
	 */
	public Result updateRangeData(RangeDataModel model){
		return rangeDataDao.updateRangeData(model);
	}
	/*
	 * 删除错误数据配置
	 */
	public Result deleRangeData(RangeDataModel model){
		return rangeDataDao.deleRangeData(model);
	}
	
	/*
	 * 根据站点  参数查询出阈值范围
	 */
	public RangeDataModel getRangeDataByStationDeviceIndicator(int stationId,int deviceId,String indicatorCode){
		return rangeDataDao.getRangeDataByStationDeviceIndicator(stationId,deviceId, indicatorCode);
	}
}
