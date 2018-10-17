package com.sdocean.resources.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.resources.dto.Resources;

@Component
public class ResourcesDao extends OracleEngine{
	
	public List<Resources> findAll() {
		String sqlString = "select r.id resid,r.name resname,r.url resurl,r.remark resremark,r.auth_id authid,a.AUTHKEY from RESOURCES r left join AUTHORITY a on r.AUTH_ID= a.id ";
		List<Resources> queryObjectList = this.queryObjectList(sqlString, null, Resources.class);
		return queryObjectList;
	}

}
