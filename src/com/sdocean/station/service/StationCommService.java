package com.sdocean.station.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.file.model.FileModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.dao.StationCommDao;
import com.sdocean.station.model.StationDeviceComm;
import com.sdocean.station.model.StationInfo;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationPictureModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class StationCommService {

	@Resource
	private StationCommDao stationCommDao;
	
	/*
	 * 为人员管理的查询结果添加表头
	 */
	public List<UiColumn> getCols4TypeList(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col0 = new UiColumn("id", "id", false, "*");
		UiColumn col1 = new UiColumn("stationId", "stationId", false, "*");
		UiColumn col2 = new UiColumn("站点名称", "stationName", true, "*");
		UiColumn col3 = new UiColumn("ip地址", "ip", true, "*");
		UiColumn col6 = new UiColumn("端口号", "port", true, "*");
		UiColumn col7 = new UiColumn("定时间隔", "cronExp", true, "*");
		UiColumn col8 = new UiColumn("上次读取时间", "lastDataTime", true, "*");
		UiColumn col9 = new UiColumn("functionName", "functionName", true, "*");
		UiColumn col10 = new UiColumn("writeTimeOut", "writeTimeOut", true, "*");
		UiColumn col11 = new UiColumn("readTimeOut", "readTimeOut", true, "*");
		//UiColumn col12 = new UiColumn("维护周期(天)", "mainCount", true, "*");
		UiColumn col13 = new UiColumn("数据表结构期限", "lastMetaDate", true, "*");
		/*UiColumn col14 = new UiColumn("数据版本", "protocalVersion", true, "*");
		UiColumn col15 = new UiColumn("模板", "templateFile", true, "*");*/
		UiColumn col17 = new UiColumn("设备", "deviceId", false, "*");
		UiColumn col18 = new UiColumn("设备", "deviceName", true, "*");
		UiColumn col19 = new UiColumn("监测间隔时间", "betweenTime", true, "*");
		
		cols.add(col0);
		cols.add(col1);
		cols.add(col2);
		cols.add(col3);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		cols.add(col10);
		cols.add(col11);
		//cols.add(col12);
		/*cols.add(col13);*/
		/*cols.add(col14);
		cols.add(col15);*/
		cols.add(col17);
		cols.add(col18);
		cols.add(col19);
		return cols;
	}
	
	/*
	 * 查询符合条件的站点配置
	 */
	public List<StationDeviceComm> getStationCommList(StationDeviceComm model){
		return stationCommDao.getStationCommList(model);
	}
	/*
	 * 保存站点配置的修改
	 */
	public Result saveStationCommChange(StationDeviceComm model){
		return stationCommDao.saveStationCommChange(model);
	}
	/*
	 * 保存新站点配置
	 */
	public Result saveNewStationComm(StationDeviceComm model){
		return stationCommDao.saveNewStationComm(model);
	}
	/*
	 * 得到该站点下的设备列表,并选中已经查询的设备
	 */
	public List<SelectTree> getDevciesByStation4Tree(StationModel model){
		return stationCommDao.getDevciesByStation4Tree(model);
	}

	/*
	 * 根据站点读取当前站点的信息
	 */
	public StationInfo getStationInfoByWpId(StationInfo model){
		return stationCommDao.getStationInfoByWpId(model);
	}
	/*
	 * 保存站点的信息展示
	 */
	public Result saveStationInfomation(StationInfo model){
		return stationCommDao.saveStationInfomation(model);
	}
	/*
	 * 删除站点信息
	 */
	public Result deleStationComm(StationDeviceComm model){
		return stationCommDao.deleStationComm(model);
	}
	/*
	 * 获得站点  的图片列表
	 */
	public List<StationPictureModel> getStationPicListByStationType(ConfigInfo info,StationPictureModel model){
		return stationCommDao.getStationPicListByStationType(info,model);
	}
	
	/*
	 * 保存站点图片
	 */
	public Result saveStationPic(StationPictureModel model,List<FileModel> files){
		return stationCommDao.saveStationPic(model, files);
	}
	
	/*
	 * 根据ID获得站点图片信息的具体信息
	 */
	public StationPictureModel getStationPicModelById(ConfigInfo info,int id){
		return stationCommDao.getStationPicModelById(info, id);
	}
	
	/*
	 * 删除图片信息
	 */
	public Result deleStationPic(StationPictureModel  model){
		return stationCommDao.deleStationPic(model);
	}
}
