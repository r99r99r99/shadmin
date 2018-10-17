package com.sdocean.dataQuery.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.dataQuery.model.SystemQueryModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.indicator.dao.IndicatorDao;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.page.model.UiColumn;

@Component
public class SystemQueryDao extends OracleEngine{

	@Resource
	IndicatorDao indicatorDao;
	
	/*
	 * 为系统数据添加表头
	 */
	public List<UiColumn> getCols4SystemQuery(SystemQueryModel model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select '时间' as displayName,'collect_time' as field,'true' as visible,'*' as width,'' as cellFilter");
		sql.append(" union all ");
		sql.append(" select distinct case when b.logo is null or length(b.logo) = 0 then a.title else concat(a.title,'(',b.logo,')') end as displayName,");
		sql.append(" a.code as field,'true' as visible,'*' as width,concat('number:',c.pointNum) as cellFilter");
		sql.append(" from dm_indicator a left join g_unit b ");
		sql.append(" on a.unitid = b.id and b.isactive = 1");
		sql.append(" ,view_stationid_deviceid_indicatorid c ");
		sql.append(" where a.id = c.indicatorid");
		sql.append(" and a.isactive = 1");
		sql.append(" and c.deviceid =").append(model.getDeviceId());
		sql.append(" and c.stationid =").append(model.getStationId());
		cols = this.queryObjectList(sql.toString(), UiColumn.class);
		return cols;
	}
	
	
	/*
	 * 为实时数据查询提供结果
	 */
	public List<Map<String, Object>> getRows4SystemQuery(SystemQueryModel model){
		List<Map<String, Object>> rows = null;
		//查询出当前设备下的参数列表
		DeviceModel device = new DeviceModel();
		device.setId(model.getDeviceId());
		List<IndicatorModel> indicators = indicatorDao.getIndicators4Deivce(device);
		
		//定义总的SQL语句
		StringBuffer sql = new StringBuffer("");
		StringBuffer selectSql = new StringBuffer(" select collect_time");
		StringBuffer fromSql = new StringBuffer(" from aiot_metadata_system");
		StringBuffer whereSql = new StringBuffer(" where");
		StringBuffer groupSql = new StringBuffer(" group by collect_time");
		StringBuffer orderSql = new StringBuffer(" order by collect_time desc");
		StringBuffer inSql = new StringBuffer("'0'");
		
		//遍历该设备的参数集合
		for(IndicatorModel indicator:indicators){
			selectSql.append(",sum(if(indicator_code='").append(indicator.getCode()).append("',data,0)) as '").append(indicator.getCode()).append("'");
			inSql.append(",'").append(indicator.getCode()).append("'");
		}
		//增加查询条件
		whereSql.append(" wpid = ").append(model.getStationId());
		whereSql.append(" and deviceid =").append(model.getDeviceId());
		whereSql.append(" and collect_time >= '").append(model.getBeginDate()).append("'");
		whereSql.append(" and collect_time <= '").append(model.getEndDate()).append("'");
		whereSql.append(" and indicator_code in (").append(inSql).append(")");
		
		sql.append(selectSql).append(fromSql).append(whereSql).append(groupSql).append(orderSql);
		rows = this.queryForList(sql.toString());
		return rows;
	}
}
