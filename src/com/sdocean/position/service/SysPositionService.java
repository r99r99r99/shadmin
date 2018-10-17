package com.sdocean.position.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.dao.SysLoginLogDao;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.position.dao.SysPositionDao;
import com.sdocean.position.model.SysPosition;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class SysPositionService {
	
	@Autowired
	private SysPositionDao positionDao;
	
	/*
	 * 获得所有有效的职位的列表
	 */
	public List<SysPosition> getPositionList(){
		return positionDao.getPositionList();
	}

}
