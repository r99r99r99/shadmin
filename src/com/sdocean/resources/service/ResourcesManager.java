package com.sdocean.resources.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.resources.dao.ResourcesDao;
import com.sdocean.resources.dto.Resources;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class ResourcesManager {
	
	@Resource
	ResourcesDao resourcesDao;
	
	@Transactional(readOnly=true)
	public List<Resources> findAll() {
		return this.resourcesDao.findAll();
	}

}
