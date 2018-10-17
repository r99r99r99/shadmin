package com.sdocean.map.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.map.dao.MapDao;
import com.sdocean.map.model.MapModel;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class MapService {

	@Resource
	private MapDao mapDao;
	
	/*
	 * 查询map表的配置信息
	 */
	public MapModel getMapConfigure(){
		return mapDao.getMapConfigure();
	}
	/*
	 * 更改系统的地图配置信息
	 */
	public Result saveMapConfig(MapModel model){
		return mapDao.saveMapConfig(model);
	}
}
