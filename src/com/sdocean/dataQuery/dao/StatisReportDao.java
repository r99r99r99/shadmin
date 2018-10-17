package com.sdocean.dataQuery.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.batik.svggen.font.table.Device;
import org.springframework.stereotype.Component;

import com.sdocean.dataQuery.model.StatisReportModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.metadata.dao.MetadataTableDao;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.page.model.UiColumn;

@Component
public class StatisReportDao extends OracleEngine{
	
	@Resource
	MetadataTableDao tableDao;
	
	/*
	 * 为平均统计查询提供表头
	 */
	public List<UiColumn> getCols4StatisReportAvg(StatisReportModel model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select '时间' as displayName,'collectTime' as field,'true' as visible,'*' as width,'' as cellFilter");
		DeviceModel device = null;
		IndicatorModel indicator = null;
		List<DeviceModel> devices = model.getDevices();
		for(DeviceModel dev:devices){
			device = dev;
			List<IndicatorModel> indicas = device.getIndicators();
			for(IndicatorModel ind:indicas){
				indicator = ind;
				//获得统计类型
				String stattypes = model.getStatTypes();
				
				if(stattypes.contains("avgdata")){
					sql.append(" union all ");
					sql.append(" select distinct case when b.logo is null or length(b.logo) = 0 then '平均值' else concat('平均值','(',b.logo,')') end as displayName,");
					sql.append(" 'avgdata' as field,'true' as visible,'*' as width,concat('number:',c.pointNum) as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());
				}
				if(stattypes.contains("mindata")){
					sql.append(" union all ");
					sql.append(" select distinct case when b.logo is null or length(b.logo) = 0 then '最小值' else concat('最小值','(',b.logo,')') end as displayName,");
					sql.append(" 'mindata' as field,'true' as visible,'*' as width,concat('number:',c.pointNum) as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());	
					
					sql.append(" union all ");
					sql.append(" select '最小值时间' as displayName,");
					sql.append(" 'mintime' as field,'true' as visible,'*' as width,'' as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());		
				}
				if(stattypes.contains("maxdata")){
					sql.append(" union all ");
					sql.append(" select distinct case when b.logo is null or length(b.logo) = 0 then '最大值' else concat('最大值','(',b.logo,')') end as displayName,");
					sql.append(" 'maxdata' as field,'true' as visible,'*' as width,concat('number:',c.pointNum) as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());
					
					sql.append(" union all ");
					sql.append(" select '最大值时间' as displayName,");
					sql.append(" 'maxtime' as field,'true' as visible,'*' as width,'' as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());
				}
				if(stattypes.contains("diffdata")){
					sql.append(" union all ");
					sql.append(" select distinct case when b.logo is null or length(b.logo) = 0 then '差值' else concat('差值','(',b.logo,')') end as displayName,");
					sql.append(" 'diffdata' as field,'true' as visible,'*' as width,concat('number:',c.pointNum) as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());
				}
				if(stattypes.contains("amplidata")){
					sql.append(" union all ");
					sql.append(" select distinct case when b.logo is null or length(b.logo) = 0 then '振幅' else concat('振幅值','(','%',')') end as displayName,");
					sql.append(" 'amplidata' as field,'true' as visible,'*' as width,concat('number:',2) as cellFilter");
					sql.append(" from dm_indicator a left join g_unit b ");
					sql.append(" on a.unitid = b.id and b.isactive = 1");
					sql.append(" ,view_stationid_deviceid_indicatorid c ");
					sql.append(" where a.id = c.indicatorid");
					sql.append(" and a.isactive = 1");
					sql.append(" and c.deviceid =").append(device.getId());
					sql.append(" and c.stationid =").append(model.getStationId());
					sql.append(" and c.indicatorid = ").append(indicator.getId());
				}
				
			}
		}
		
		cols = this.queryObjectList(sql.toString(), UiColumn.class);
		return cols;
	}
	/*
	 * 查询参数在一定的统计口径内的平均值
	 */
	public List<StatisReportModel> getRowsStatisReportDatas(StatisReportModel model){
		List<StatisReportModel> rows = null;
		DeviceModel device = null;
		IndicatorModel indicator = null;
		List<DeviceModel> devices = model.getDevices();
		for(DeviceModel dev:devices){
			device = dev;
			List<IndicatorModel> indicas = device.getIndicators();
			for(IndicatorModel ind:indicas){
				indicator = ind;
			}
		}
		
		StringBuffer sql= new StringBuffer("");
		sql.append("call getRowsStatisReportDatas(").append(model.getStationId()).append(",").append(device.getId()).append(",'").append(indicator.getCode()).append("'");
		sql.append(",'").append(model.getStatTypes()).append("',").append(model.getCollectType()).append(",'").append(model.getBeginDate()).append("','").append(model.getEndDate()).append("')");
		rows = this.pricForObjects(sql.toString(), StatisReportModel.class);
		
		return rows;
	}
}
