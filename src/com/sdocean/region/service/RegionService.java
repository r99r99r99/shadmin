package com.sdocean.region.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.SelectTree;
import com.sdocean.region.dao.RegionDao;
import com.sdocean.region.model.RegionModel;
import com.sdocean.station.model.StationModel;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class RegionService {
	
	@Autowired
	private RegionDao regionDao;
	
	/*
	 * 在下拉框中展示站点树
	 */
	public List<SelectTree> getRegionList4Tree(StationModel station){
		return regionDao.getRegionList4Tree(station);
	}
	
	/*
	 * 根据地区ID获得上级所有的地区信息
	 */
	public List<RegionModel> getRegionListById(int id){
		return regionDao.getRegionListById(id);
	}
}
