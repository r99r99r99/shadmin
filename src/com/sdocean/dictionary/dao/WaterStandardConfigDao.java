package com.sdocean.dictionary.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.common.model.ZTreeModel;
import com.sdocean.dictionary.model.UnitGroupModel;
import com.sdocean.dictionary.model.WaterStandardConfig;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.role.model.RoleModel;
import com.sdocean.station.model.StationModel;
import com.sdocean.station.model.StationTypeModel;
import com.sdocean.users.model.SysUser;

@Component
public class WaterStandardConfigDao extends OracleEngine {
	
	/*
	 * 查询水质等级配置列表
	 */
	public List<WaterStandardConfig> getWaterStandardConfigList(WaterStandardConfig model){
		//初始化返回结果
		List<WaterStandardConfig> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.typeid,b.value as typename,a.classid,a.classname,a.color,a.ordercode");
		sql.append(" from g_waterstandard_config a, sys_public b");
		sql.append(" where b.parentcode = '0005'");
		sql.append(" and a.typeid = b.classid");
		//添加查询条件
		if(model!=null&&model.getTypeId()>0){
			sql.append(" and a.typeid = ").append(model.getTypeId());
		}
		sql.append(" order by b.ordercode,a.ordercode");
		list = this.queryObjectList(sql.toString(), WaterStandardConfig.class);
		return list;
	}
	
}
