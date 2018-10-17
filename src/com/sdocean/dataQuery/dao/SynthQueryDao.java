package com.sdocean.dataQuery.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.indicator.dao.IndicatorDao;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.metadata.dao.MetadataTableDao;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.page.model.UiColumn;
import com.sdocean.station.model.StationModel;

@Component
public class SynthQueryDao extends OracleEngine{
	
	@Resource
	MetadataTableDao tableDao;
	@Resource
	IndicatorDao indicatorDao;
	/*
	 * 为综合查询添加表头
	 */
	public List<UiColumn> getCols4SynQuery(DataQueryModel model){
		
		List<DeviceModel> devices = model.getDevices();
		
		
		List<UiColumn> cols = new ArrayList<UiColumn>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select '时间' as displayName,'collect_time' as field,'true' as visible,'*' as width,'' as cellFilter");
		for(DeviceModel device:devices){
			//遍历设备内的参数
			StringBuffer indicatorSql = new StringBuffer("(0");
			for(IndicatorModel indicator:device.getIndicators()){
				indicatorSql.append(",").append(indicator.getId());
			}
			indicatorSql.append(")");
			sql.append(" union all ");
			sql.append(" select case when b.logo is null or length(b.logo)=0 then a.title else concat(a.title,'(',b.logo,')') end  as displayName,");
			sql.append(" concat(a.code,c.catalogid) as field,'true' as visible,'*' as width,concat('number:',d.pointNum) as cellFilter");
			sql.append(" from dm_indicator a left join g_unit b ");
			sql.append(" on a.unitid = b.id and b.isactive = 1");
			sql.append(" ,device_catalog_indicator c,device_catalog d");
			sql.append(" where a.id = c.indicatorid");
			sql.append(" and c.catalogid = d.id");
			sql.append(" and a.isactive = 1");
			sql.append(" and c.catalogid =").append(device.getId());
			sql.append(" and a.id in ").append(indicatorSql);
		}
		cols = this.queryObjectList(sql.toString(), UiColumn.class);
		return cols;
	}
	
	/*
	 * 为综合查询查询结果
	 */
	public List<Map<String, Object>> getRows4SynQuery(DataQueryModel model){
		List<Map<String, Object>> rows = null;
		StationModel station = new StationModel();
		station.setId(model.getStationId());
		//根据起始时间判断出需要查询的表名的集合
		List<MetadataTable> tables = tableDao.getTables4Meta(station,model.getBeginDate(), model.getEndDate(), 2);
		//定义总的查询语句
		StringBuffer sql = new StringBuffer("");
		//定义select部分
		StringBuffer selectSql = new StringBuffer(" select collect_time ");
		//定义where部分
		StringBuffer whereSql = new StringBuffer(" where ");
		whereSql.append(" wpid = ").append(model.getStationId()).append(" and (1=0 ");
		//定义groupby部分
		StringBuffer groupSql = new StringBuffer(" group by collect_time");
		//定义having部分
		StringBuffer havingSql = new StringBuffer(" having 1=0 ");
		//定义排序部分
		StringBuffer orderSql = new StringBuffer(" order by collect_time desc");
		List<DeviceModel> devices = model.getDevices();
		for(DeviceModel device:devices){
			List<IndicatorModel> indicators = device.getIndicators();
			StringBuffer indicatorCodes = new StringBuffer("'0'");
			for(IndicatorModel indicator:indicators){
				selectSql.append(", sum(if(indicator_code='").append(indicator.getCode()).append("' and deviceid = ").append(device.getId()).append(",data,0)) as ");
				selectSql.append(indicator.getCode()).append(device.getId());
				
				indicatorCodes.append(",'").append(indicator.getCode()).append("'");
				
				havingSql.append("||sum(if(indicator_code='").append(indicator.getCode()).append("' and deviceid =").append(device.getId()).append(",data,0)) <> 0");
			}
			whereSql.append(" or ( deviceid = ").append(device.getId()).append(" and indicator_code in (").append(indicatorCodes).append("))");
		}
		whereSql.append(")");
		
		//添加时间查询条件
		//增加时间参数.
		if(model.getBeginDate()!=null){
			whereSql.append(" and collect_time >= '").append(model.getBeginDate()).append("'");
		}
		if(model.getEndDate()!=null){
			whereSql.append(" and collect_time <= '").append(model.getEndDate()).append("'");
		}	
		
		//遍历TABLELIST组成查询语句
		for(int i=0;i<tables.size();i++){
			String tablename = tables.get(i).getTableName();
			String fromSql = " from "+tablename;
			sql.append(selectSql).append(fromSql).append(whereSql).append(groupSql).append(havingSql);
			if(i<tables.size()-1){
				sql.append(" union all ");
			}
		}
		sql.append(orderSql);
		rows = this.queryForList(sql.toString());
		return rows;
	}
	
}
