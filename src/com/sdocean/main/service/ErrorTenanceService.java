package com.sdocean.main.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.main.dao.ErrorTenanceDao;
import com.sdocean.main.dao.MainTenanceDao;
import com.sdocean.main.model.ErrorTenance;
import com.sdocean.main.model.MainTenance;
import com.sdocean.main.model.MainTenanceFile;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ErrorTenanceService {

	@Resource
	private ErrorTenanceDao errorDao;

	public List<UiColumn> getCols4MainEditList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col0 = new UiColumn("id", "id", false, "*");
		UiColumn col1 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col2 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col3 = new UiColumn("deviceId", "deviceIds", false, "*");
		UiColumn col4 = new UiColumn("设备名称", "deviceNames", true, "*");
		UiColumn col6 = new UiColumn("设备状态", "stateName", true, "*");
		UiColumn col8 = new UiColumn("维护开始时间", "beginTime", true, "*");
		UiColumn col9 = new UiColumn("维护结束时间", "endTime", true, "*");
		UiColumn col10 = new UiColumn("推测原因", "reason", true, "*");
		UiColumn col7 = new UiColumn("异常问题", "error", true, "*");
		UiColumn col11 = new UiColumn("维护结果", "result", true, "*");
		UiColumn col12 = new UiColumn("需求材料", "material", true, "*");
		UiColumn col13 = new UiColumn("userId", "userId", false, "*");
		UiColumn col14 = new UiColumn("上报人", "userName", true, "*");
		UiColumn col15 = new UiColumn("上报时间", "collectTime", true, "*");
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col6);
		cols.add(col8);
		cols.add(col9);
		cols.add(col7);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
		cols.add(col13);
		cols.add(col14);
		cols.add(col15);
		return cols;
	}
	/*
	 * 查询出查询条件下的异常维护列表
	 */
	public List<ErrorTenance> getErrorsByStation(ErrorTenance model,List<StationModel> stations){
		return errorDao.getErrorsByStation(model, stations);
	}
	/*
	 * 增加新的异常维护记录
	 */
	public Result saveNewErrorTenance(ErrorTenance model){
		return errorDao.saveNewErrorTenance(model);
	}
	/*
	 * 修改异常维护上报信息
	 */
	public Result saveChangeErrorTenance(ErrorTenance model){
		return errorDao.saveChangeErrorTenance(model);
	}
	/*
	 * 为异常维护记录提供设备列表
	 */
	public List<SelectTree> getDeviceList4Error(ErrorTenance model){
		return errorDao.getDeviceList4Error(model);
	}
}
