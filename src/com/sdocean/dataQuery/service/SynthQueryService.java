package com.sdocean.dataQuery.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.SelectTree;
import com.sdocean.dataQuery.dao.DataQueryDao;
import com.sdocean.dataQuery.dao.SynthQueryDao;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.device.dao.DeviceDao;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.dao.PublicDao;
import com.sdocean.dictionary.model.PublicModel;
import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.dao.SysLoginLogDao;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.page.model.NgColumn;
import com.sdocean.page.model.UiColumn;
import com.sdocean.position.dao.SysPositionDao;
import com.sdocean.position.model.SysPosition;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class SynthQueryService {
	
	@Autowired
	private SynthQueryDao synthQueryDao;
	
	@Autowired
	private DeviceDao deviceDao;
	
	
	/*
	 * 为综合查询添加表头
	 */
	public List<UiColumn> getCols4SynQuery(DataQueryModel model){
		return synthQueryDao.getCols4SynQuery(model);
	}
	
	
	/*
	 * 为综合查询查询结果
	 */
	public List<Map<String, Object>> getRows4SynQuery(DataQueryModel model){
		return synthQueryDao.getRows4SynQuery(model);
	}
}
