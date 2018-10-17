package com.sdocean.dataQuery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dataQuery.dao.DataQueryDao;
import com.sdocean.dataQuery.model.DataChangeModel;
import com.sdocean.dataQuery.model.DataCheckModel;
import com.sdocean.dataQuery.model.DataImportModel;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.ManualDataModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.firstpage.model.LastMetaData;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.dao.IndicatorDao;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.metadata.dao.MetadataDao;
import com.sdocean.metadata.dao.MetadataTableDao;
import com.sdocean.metadata.model.MetadataModel;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.page.model.PageResult;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class DataQueryService {
	
	@Autowired
	private DataQueryDao dataQueryDao;
	@Autowired
	private MetadataDao metaDataDao;
	@Autowired
	private MetadataTableDao tableDao;
	@Autowired
	private IndicatorDao indicatorDao;
	/*
	 * 为实时数据添加表头
	 */
	public List<UiColumn> getCols4DataQuery(DataQueryModel model){
		return dataQueryDao.getCols4DataQuery(model);
	}
	
	/*
	 * 为实时数据查询提供结果
	 */
	public List<Map<String, Object>> getRows4DataQuery(DataQueryModel model){
		return dataQueryDao.getRows4DataQuery(model);
	}
	
	/*
	 * 查询出当前站点信息的最后实时数据
	 * 为首页展示
	 */
	public List<LastMetaData> getData4FirstPage(StationModel station){
		return dataQueryDao.getData4FirstPage(station);
	}
	
	/*
	 * 为数据修改查询结果提供表头
	 */
	public List<UiColumn> getCols4DataChange(DataChangeModel model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*", false, "number");
		UiColumn col2 = new UiColumn("时间", "collect_time", true, "*", false, "");
		//col2.setCellFilter("data:\"yyyy-MM-dd HH:mm:ss.0\"");
		UiColumn col3 = new UiColumn("数值", "data", true, "*", true, "number");
		//UiColumn col4 = new UiColumn("异常", "markCode", true, "*", true, "");
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		//cols.add(col4);
		return cols;
	}
	/*
	 * 为数据修改的查询提供结果
	 */
	public List<MetadataModel> getResult4DataChangeshow(DataChangeModel model){
		return dataQueryDao.getResult4DataChangeshow(model);
	}
	/*
	 * 保存修改
	 */
	public Result saveChangeData(DataChangeModel model){
		return dataQueryDao.saveChangeData(model);
	}
	/*
	 * 保存数据导入
	 */
	public Result saveImportData(DataImportModel model,SysUser user){
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.FAILED);
		result.setMessage("导入数据成功");
		
		List<DataImportModel> list = new ArrayList<DataImportModel>();
		try {
			list = (List<DataImportModel>) JsonUtil.fromJsons(model.getImportString(), DataImportModel.class);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("数据错误");
			return result;
		}
		if(list==null||list.size()<1){
			result.setResult(Result.FAILED);
			result.setMessage("数据错误");
			return result;
		}
		//将其他形式的元数据转换成标准元数据
		List<MetadataModel> metas = new ArrayList<MetadataModel>();
		metas = metaDataDao.changeMetadata(list, 1, "csvImport");
		//整合数据采集时间
		for(MetadataModel meta:metas){
			meta = metaDataDao.changeNewMetadata(meta);
			StationModel station = new StationModel();
			station.setId(meta.getWpId());
			//将标准元数据存入到基础元数据表中
			MetadataTable metaTable = tableDao.getOneTable(station, meta.getCollect_time(), 1);
			
			DataChangeModel dmodel = new DataChangeModel();
			dmodel.setCollect_time(meta.getCollect_time());
			dmodel.setStationId(meta.getWpId());
			dmodel.setDeviceId(meta.getDeviceId());
			dmodel.setIndicatorCode(meta.getIndicator_code());
			dmodel.setNewData(meta.getData());
			dmodel.setUserId(user.getId());
			dataQueryDao.saveChangeDataLog(dmodel);
			try {
				metaDataDao.saveMetaData(metaTable.getTableName(), meta);
			} catch (Exception e) {
				// TODO: handle exception
				result.setResult(Result.FAILED);
				return result;
			}
			//将标准元数据存入到综合元数据表中
			MetadataTable  synTable = tableDao.getOneTable(station, meta.getCollect_time(), 2);
			try {
				metaDataDao.saveSynData(synTable.getTableName(), meta);
			} catch (Exception e) {
				// TODO: handle exception
				result.setResult(Result.FAILED);
				result.setMessage("导入综合元数据表错误");
				return result;
			}
		}
		return result;
	}
	
	/*
	 * 为数据修改日志查询提供表头
	 * 
	 */
	public List<UiColumn> getDataChangeLogCols(DataChangeModel model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*", false, "number");
		UiColumn col2 = new UiColumn("时间", "collect_time", true, "*", true, "");
		UiColumn col3 = new UiColumn("wpid", "stationId", false, "*", false, "");
		UiColumn col4 = new UiColumn("站点", "stationName", true, "*", false, "");
		UiColumn col5 = new UiColumn("deviceId", "deviceId", false, "*", false, "");
		UiColumn col6 = new UiColumn("设备", "deviceName", true, "*", false, "");
		UiColumn col7 = new UiColumn("indicatorCode", "indicatorCode", false, "*", false, "");
		UiColumn col8 = new UiColumn("参数", "indicatorName", true, "*", false, "");
		UiColumn col9 = new UiColumn("原数据", "oldData", true, "*", false, "");
		UiColumn col10 = new UiColumn("新数据", "newData", true, "*", false, "");
		UiColumn col11 = new UiColumn("changeType", "changeType", false, "*", false, "");
		UiColumn col12 = new UiColumn("变更类型", "changeTypeName", true, "*", false, "");
		UiColumn col13 = new UiColumn("userId", "userId", false, "*", false, "");
		UiColumn col14 = new UiColumn("操作人员", "userName", true, "*", false, "");
		UiColumn col15 = new UiColumn("修改时间", "changeTime", true, "*", false, "");
		
		
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col11);
		cols.add(col12);
		cols.add(col13);
		cols.add(col14);
		cols.add(col15);
		return cols;
	}
	
	/*
	 * 得到数据修改日志的数据列表
	 */
	public List<DataChangeModel> getDataChangeLogRows(DataChangeModel model){
		return dataQueryDao.getDataChangeLogRows(model);
	}
	
	/**
	 * 根据站点和设备列表,查询权限下的参数列表,并整合成cols
	 */
	public List<UiColumn> getColByStationDevice(StationModel station,DeviceModel device){
		List<UiColumn> list = new ArrayList<>();
		//添加时间列
		UiColumn timeCol = new UiColumn("时间", "collect_time", true, "*", true, "");
		list.add(timeCol);
		//根据站点/设备获得参数的列表
		List<IndicatorModel> indicators = indicatorDao.getIndicatorsByStationDevcie(station, device);
		for(IndicatorModel indicator:indicators){
			UiColumn col = new UiColumn(indicator.getTitle(),indicator.getCode(),true,"*", true, "number");
			list.add(col);
		}
		return list;
	}
	
	/*
	 * 为手动导入数据查询提供表头
	 */
	public List<UiColumn> getManualDataCols(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col3 = new UiColumn("站点", "stationName", false, "*");
		UiColumn col4 = new UiColumn("indicatorCode", "indicatorCode", false, "*");
		UiColumn col5 = new UiColumn("参数", "indicatorName", false, "*");
		UiColumn col6 = new UiColumn("采集时间", "collectTime", true, "*");
		UiColumn col7 = new UiColumn("数值", "data", true, "*");
		UiColumn col8 = new UiColumn("上报时间", "createTime", false, "*");
		UiColumn col9 = new UiColumn("userId", "userId", false, "*");
		UiColumn col10 = new UiColumn("上报人", "userName", false, "*");
		cols.add(col1);
		cols.add(col6);
		cols.add(col7);
		return cols;
	}
	
	/*
	 * 查询手动导入数据列表
	 */
	public List<ManualDataModel> getManualDataList(ManualDataModel model){
		return dataQueryDao.getManualDataList(model);
	}
	
	/*
	 * 整合传输的数据
	 */
	public List<ManualDataModel> handleManualDataList(int stationId,String indicatorCode,String dataText,SysUser user){
		List<ManualDataModel> list = new ArrayList<ManualDataModel>();
		list = JsonUtil.fromJsons(dataText, ManualDataModel.class);
		for(ManualDataModel model:list) {
			model.setStationId(stationId);
			model.setIndicatorCode(indicatorCode);
			model.setUserId(user.getId());
		}
		return list;
	}
	/*
	 * 手动导入保存的数据
	 */
	public Result saveManualData(List<ManualDataModel> models) {
		return dataQueryDao.saveManualData(models);
	}
	/*
	 * 删除手动添加的数据
	 */
	public Result deleManualData(ManualDataModel model) {
		return dataQueryDao.deleManualData(model);
	}
	
	/*
	 * 保存备注操作
	 */
	public Result saveChangeMark(DataChangeModel model){
		return dataQueryDao.saveChangeMark(model);
	}
	
	/*
	 * 为数据审核查询结果提供表头
	 */
	public List<UiColumn> getCols4DataCheck(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*", "number");
		UiColumn col2 = new UiColumn("时间", "collect_time", true, "*", "");
		//col2.setCellFilter("data:\"yyyy-MM-dd HH:mm:ss.0\"");
		UiColumn col3 = new UiColumn("数值", "data", true, "*", "number");
		UiColumn col4 = new UiColumn("isactive", "isactive", false, "*", "");
		UiColumn col5 = new UiColumn("状态", "isactiveName", true, "*", "");
		UiColumn col6 = new UiColumn("备注", "remark", true, "*", "");
		UiColumn col7 = new UiColumn("tableName", "tableName", false, "*", "");
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
	 * 为数据审核查询提供结果集
	 */
	public List<MetadataModel> getResult4DataCheckshow(DataCheckModel model){
		return dataQueryDao.getResult4DataCheckshow(model);
	}
	
	/*
	 * 保存数据审核结果
	 */
	public Result saveDataCheckResult(DataCheckModel model){
		return dataQueryDao.saveDataCheckResult(model);
	}
	
}
