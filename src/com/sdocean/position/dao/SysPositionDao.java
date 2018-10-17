package com.sdocean.position.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.position.model.SysPosition;
import com.sdocean.users.model.SysUser;

@Component
public class SysPositionDao extends OracleEngine{
	
	/*
	 * 得到所有有效的职位列表
	 */
	public List<SysPosition> getPositionList(){
		List<SysPosition> positions = new ArrayList<SysPosition>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.code,a.name,a.remark,a.isactive,");
		sql.append(" case a.isactive when 1 then '启用' else '禁用' end as isactivename,");
		sql.append(" a.ordercode from sys_position a where a.isactive = 1 order by ordercode");
		positions = this.queryObjectList(sql.toString(), SysPosition.class);
		return positions;
	}
	 
}
