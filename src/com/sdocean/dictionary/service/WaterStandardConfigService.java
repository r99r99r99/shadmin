package com.sdocean.dictionary.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.dictionary.dao.UnitDao;
import com.sdocean.dictionary.dao.WaterStandardConfigDao;
import com.sdocean.dictionary.model.UnitModel;
import com.sdocean.dictionary.model.WaterStandardConfig;
import com.sdocean.page.model.UiColumn;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class WaterStandardConfigService {

	@Resource 
	WaterStandardConfigDao waterStandardConfigDao;
	/*
	 * 查询水质等级配置列表
	 */
	public List<WaterStandardConfig> getWaterStandardConfigList(WaterStandardConfig model){
		return waterStandardConfigDao.getWaterStandardConfigList(model);
	}
	
}
