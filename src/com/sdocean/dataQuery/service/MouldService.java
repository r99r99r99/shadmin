package com.sdocean.dataQuery.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dataQuery.dao.DataQueryDao;
import com.sdocean.dataQuery.dao.MouldDao;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.dataQuery.model.MouldModel;
import com.sdocean.firstpage.model.LastMetaData;
import com.sdocean.page.model.NgColumn;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class MouldService {
	
	@Autowired
	private MouldDao mouldDao;
	/*
	 * 获得当前站点的水质评价模板
	 */
	public MouldModel getMouldByStationId(MouldModel model){
		return mouldDao.getMouldByStationId(model);
	}
	/*
	 * 保存当前站点的水质评价模板
	 */
	public Result saveMouldSetting(MouldModel model){
		return mouldDao.saveMouldSetting(model);
	}
	
	/*
	 * 根据站点,获得水质评价模板
	 */
	public MouldModel getMouldByStation(StationModel model){
		return mouldDao.getMouldByStation(model);
	}
}
