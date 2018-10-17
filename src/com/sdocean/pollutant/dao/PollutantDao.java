package com.sdocean.pollutant.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.dataQuery.model.DataQueryModel;
import com.sdocean.device.model.DeviceModel;
import com.sdocean.dictionary.dao.UnitDao;
import com.sdocean.dictionary.model.UnitModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.menu.model.SysMenu;
import com.sdocean.page.model.UiColumn;
import com.sdocean.pollutant.model.Pollutant4First;
import com.sdocean.pollutant.model.PollutantModel;
import com.sdocean.position.model.SysPosition;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Component
public class PollutantDao extends OracleEngine{
	
	@Resource
	UnitDao unitDao;
	/*
	 * 为首页的入海污染物查询提供表头
	 */
	public List<UiColumn> getColumns4Polluinfo4First(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("时间", "collect_time", true, "*");
		UiColumn col0 = new UiColumn("水量", "flowall", true, "*");
		UiColumn col2 = new UiColumn("总磷", "zonglin", true, "*");
		UiColumn col3 = new UiColumn("总氮", "zongdan", true, "*");
		UiColumn col4 = new UiColumn("硝酸盐", "xiaosuanyan", true, "*");
		UiColumn col5 = new UiColumn("亚硝酸盐", "ya", true, "*");
		UiColumn col6 = new UiColumn("氨氮", "andan", true, "*");
		UiColumn col7 = new UiColumn("磷酸盐", "andan", true, "*");
		UiColumn col8 = new UiColumn("COD", "cod", true, "*");
		UiColumn col9 = new UiColumn("石油", "石油", true, "*");
		//UiColumn col1 = new UiColumn("时间", "collect_time", true, "*");
		cols.add(col1);
		cols.add(col0);
		cols.add(col2);
		cols.add(col3);
		cols.add(col4);
		cols.add(col5);
		cols.add(col6);
		cols.add(col7);
		cols.add(col8);
		cols.add(col9);
		return cols;
	}
	
	
	
	/*
	 * 为入海污染量查询提供表头
	 */
	public List<UiColumn> getColumns4Polluinfo(PollutantModel model){
		//获得换算单位的信息
		UnitModel unit = unitDao.getUnitByCode(model.getUnit());
		List<DeviceModel> devices = model.getDevices();
		List<UiColumn> cols = new ArrayList<UiColumn>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select '时间' as displayName,'collect_time' as field,'true' as visible,'*' as width");
		sql.append(" union all");
		sql.append(" select '瞬时流速(立方米/秒)' as displayName,'Flow' as field,'false' as visible,'*' as width");
		sql.append(" union all");
		sql.append(" select '水量(立方米)' as displayName,'FlowAll' as field,'true' as visible,'*' as width");
		for(DeviceModel device:devices){
			//遍历设备内的参数
			StringBuffer indicatorSql = new StringBuffer("(0");
			for(IndicatorModel indicator:device.getIndicators()){
				indicatorSql.append(",").append(indicator.getId());
			}
			indicatorSql.append(")");
			
			sql.append(" union all ");
			
			sql.append(" select concat(a.title,'(',b.name,')') as displayName,"); 
			//sql.append(" select case when b.toUnitLogo is null or length(b.toUnitLogo)=0 then a.title else concat(a.title,'(',b.toUnitLogo,')') end  as displayName,");
			sql.append(" concat(a.code,'_',c.catalogid) as field,'false' as visible,'*' as width");
			sql.append(" from dm_indicator a left join g_unit b on a.unitid = b.id");
			sql.append(" ,device_catalog_indicator c");
			sql.append(" where a.id = c.indicatorid");
			sql.append(" and a.isactive = 1");
			sql.append(" and c.catalogid =").append(device.getId());
			sql.append(" and a.id in ").append(indicatorSql);
			
			sql.append(" union all ");
			
			sql.append(" select concat(a.title,'(','").append(unit.getName()).append("',')') as displayName,"); 
			//sql.append(" select case when b.toUnitLogo is null or length(b.toUnitLogo)=0 then a.title else concat(a.title,'(',b.toUnitLogo,')') end  as displayName,");
			sql.append(" concat(a.code,c.catalogid) as field,'true' as visible,'*' as width");
			sql.append(" from dm_indicator a");
			sql.append(" ,device_catalog_indicator c");
			sql.append(" where a.id = c.indicatorid");
			sql.append(" and a.isactive = 1");
			sql.append(" and c.catalogid =").append(device.getId());
			sql.append(" and a.id in ").append(indicatorSql);
		}
		//添加所有污染物的总和
		sql.append(" union all ");
		sql.append(" select '总量(").append(unit.getName()).append(")' as displayName,'allpop' as field,'true' as visible,'*' as width ");
		cols = this.queryObjectList(sql.toString(), UiColumn.class);
		return cols;
	}
	
	/*
	 * 为入海污染量查询提供结果集
	 */
	public List<Map<String, Object>> getRows4Polluinfo(PollutantModel model){
		//需要计算返回的单位
		String unit = model.getUnit();
		//得到单位
		String indicatorCode = model.getDevices().get(0).getIndicators().get(0).getCode();
		
		
		//根据查询的参数以及目标单位,获得单位层级
		StringBuffer unitSql = new StringBuffer("");
		unitSql.append(" select c.multiply");
		unitSql.append(" from dm_indicator a,g_unit b,g_unit_unit c");
		unitSql.append(" where a.unitid = b.id ");
		unitSql.append(" and b.code = c.fromunit");
		unitSql.append(" and a.code = '").append(indicatorCode).append("'");
		unitSql.append(" and c.toUnit = '").append(unit).append("'");
		UnitModel unitm = this.queryObject(unitSql.toString(), UnitModel.class);
		Double multiply = (double) 1;
		if(unitm==null||unitm.getMultiply()==null||unitm.getMultiply()==0){
			multiply = (double) 1;
		}else{
			multiply = unitm.getMultiply();
		}
		
		List<Map<String, Object>> rows = null;
		//初始化总的sql语句
		StringBuffer sql = new StringBuffer("");
		//初始化select语句
		StringBuffer selectSql = new StringBuffer("");
		//初始化select语句中的计算中和部分
		StringBuffer allpolsql = new StringBuffer(",round((0");
		//初始化from语句
		StringBuffer fromSql = new StringBuffer("");
		//初始化where语句
		StringBuffer whereSql = new StringBuffer(" where 1 = 1");
		//初始化group语句
		StringBuffer groupSql = new StringBuffer("");
		//初始化order语句
		StringBuffer orderSql = new StringBuffer("");
		//统计口径
		String datesql = "";
		//当天统计口径下有多少天
		String dayNum = "1";
		//一天的秒数
		String seconds = "86400";
		
		//获得统计口径
		int type = model.getType();
		if(type==1){
			datesql = "date_format(a.collectDate,'%Y-%m-%d')";
			dayNum = "1";
		}else if(type==2){
			datesql = "concat(year(a.collectDate),week(a.collectDate))";
			//datesql = "week(a.collectDate)";
			dayNum = "7";
		}else if(type==3){
			datesql = "date_format(a.collectDate,'%Y-%m')";
			dayNum = "day(last_day(date_format(a.collectDate,'%Y-%m-01')))";
		}
		//计算瞬时流量
		String flow = "avg(case a.indicatorCode when 'Flow' then a.avgData end)";
		//计算统计口径下的水通量
		String flowAll = "avg(case a.indicatorCode when 'Flow' then a.avgData end) *"+dayNum+"* 86400";
		//编辑select部分
		selectSql.append(" select ").append(datesql).append(" as collect_time,");
		selectSql.append(dayNum).append(" as dayNum,");
		selectSql.append("round(").append(flow).append(",2) as Flow,");
		selectSql.append("round(").append(flowAll).append(",2) as FlowAll");
		
		//编辑from部分
		fromSql.append(" from aiot_pollutant_avg a,dm_indicator b ");
		
		//编辑where部分
		whereSql.append(" and a.indicatorcode = b.code ");
		whereSql.append(" and b.isactive = 1");
		//添加站点限制
		whereSql.append(" and wpid = ").append(model.getWpId());
		//添加时间查询条件
		if(model.getBeginDate()!=null&&model.getBeginDate().length()>0){
			whereSql.append(" and a.collectDate >= '").append(model.getBeginDate()).append("'");
		}
		if(model.getEndDate()!=null&&model.getEndDate().length()>0){
			whereSql.append(" and a.collectDate <= '").append(model.getEndDate()).append("'");
		}
		//编辑groupby部分
		if(type==2){
			groupSql.append(" group by ").append(datesql);
		}else{
			groupSql.append(" group by ").append(datesql).append(",").append(dayNum);
		}
		
		//编辑orderby部分
		orderSql.append(" order by ").append(datesql).append(" desc");
		//得到设备以及参数列表
		List<DeviceModel> devices = model.getDevices();
		for(DeviceModel device:devices){
			//得到该设备下需要查询的参数列表
			List<IndicatorModel> indicators = device.getIndicators();
			for(IndicatorModel indicator:indicators){
				selectSql.append(",avg(case when a.deviceid= ").append(device.getId());
				selectSql.append(" and a.indicatorCode = '").append(indicator.getCode()).append("' then a.avgData end) as ");
				selectSql.append(indicator.getCode()).append("_").append(device.getId());
				
				selectSql.append(",").append("round(").append(flowAll).append("* avg(case when a.deviceid= ").append(device.getId());
				selectSql.append(" and a.indicatorCode = '").append(indicator.getCode()).append("' then a.avgData end)");
				selectSql.append(" * ").append(multiply).append(",2) as ").append(indicator.getCode()).append(device.getId());
				
				allpolsql.append(" + ifnull(").append(flowAll).append("* avg(case when a.deviceid= ").append(device.getId());
				allpolsql.append(" and a.indicatorCode = '").append(indicator.getCode()).append("' then a.avgData end)");
				allpolsql.append(" * ").append(multiply).append(",0)");
			}
		}
		allpolsql.append("),2) as allpop");
		//整合sql语句 
		sql.append(selectSql).append(allpolsql).append(fromSql).append(whereSql).append(groupSql).append(orderSql);
		rows = this.queryForList(sql.toString());
		return rows;
	}
	
	/*
	 * 为首页展示入海污染量提供数据
	 */
	public Pollutant4First getPollutants4FirstPage(StationModel station){
		Pollutant4First first = new Pollutant4First();
		//得到当前站点的最后的时间,以及水通量
		StringBuffer firstSql = new StringBuffer("");
		firstSql.append(" select wpid,indicatorCode,avgData as avgFlow,avgData*1*86400 as allFlow,collectDate");
		firstSql.append(" from aiot_pollutant_avg where  indicatorCode = 'Flow' and wpid = ").append(station.getId());
		firstSql.append(" order by collectDate desc limit 1");
		first = this.queryObject(firstSql.toString(), Pollutant4First.class);
		//获得每个参数的入海污染量信息
		StringBuffer polluSql = new StringBuffer("");
		polluSql.append(" select b.code as indicatorCode ,b.title as indicatorName,a.avgData,");
		polluSql.append(" round(a.avgData*").append(first.getAllFlow()).append("*g.multiply,4) as polluData,k.logo as unit");
		polluSql.append(" from aiot_pollutant_avg a,dm_indicator b,");
		polluSql.append(" aiot_station_device_indicator c,aiot_firstpage_show d,");
		polluSql.append(" aiot_pollutant_indicator e,g_unit f,g_unit_unit g,g_unit k");
		polluSql.append(" where a.wpid =").append(station.getId()).append(" and b.isactive = 1");
		polluSql.append(" and a.collectDate = '").append(first.getCollectDate()).append("' and g.toUnit = 'kg'");
		polluSql.append(" and e.type = 1");
		polluSql.append(" and a.indicatorcode = b.code");
		polluSql.append(" and a.wpid = c.stationid and a.wpid = d.wpId and a.wpid = e.wpid");
		polluSql.append(" and a.deviceid = c.deviceid and a.deviceid = d.deviceid and a.deviceid = e.deviceid");
		polluSql.append(" and b.id = c.indicatorid and b.id = d.indicatorid  and b.code = e.indicatorCode");
		polluSql.append(" and b.unitid = f.id and f.code = g.fromunit");
		polluSql.append(" and g.tounit = k.code ");
		List<PollutantModel> list = new ArrayList<PollutantModel>();
		list = this.queryObjectList(polluSql.toString(), PollutantModel.class);
		first.setPolluList(list);
		return first;
	}
	
	/*
	 * 为入海污染量设置查询提供结果集
	 */
	public List<PollutantModel> getPolluSetList(PollutantModel model){
		List<PollutantModel> list = new ArrayList<PollutantModel>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.wpid,b.title as wpname,a.deviceid,c.name as devicename,");
		sql.append(" a.indicatorCode,d.title as indicatorName,a.type,e.value as typename");
		sql.append(" from aiot_pollutant_indicator a,aiot_watch_point b,");
		sql.append(" device_catalog c,dm_indicator d,sys_public e");
		sql.append(" where a.wpid = b.id   ");
		if(model.getWpId()>0){
			sql.append(" and wpid = ").append(model.getWpId());
		}
		sql.append(" and a.deviceid = c.id");
		sql.append(" and a.indicatorcode = d.code and d.isactive = 1");
		sql.append(" and a.type = e.classid and e.parentcode = '0013'");
		list = this.queryObjectList(sql.toString(), PollutantModel.class);
		return list;
	}
	
	/*
	 * 添加入海污染源设置
	 */
	public Result saveNewPolluSet(PollutantModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.ADD);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("新增成功");
		//唯一性检查
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_pollutant_indicator");
		checkSql.append(" where wpid = ").append(model.getWpId());
		checkSql.append(" and deviceid = ").append(model.getDeviceId());
		checkSql.append(" and indicatorCode = '").append(model.getIndicatorCode()).append("'");
		int check = 0;
		try {
			check = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("唯一性检查时失败");
			return result;
		}
		if(check>0){
			result.setResult(Result.FAILED);
			result.setMessage("违反唯一性原则");
			return result;
		}
		//执行插入语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_pollutant_indicator(wpid,deviceid,indicatorcode,type)");
		sql.append(" values(?,?,?,?)  on duplicate key update type = values(type)");
		Object[] params = new Object[]{
				model.getWpId(),model.getDeviceId(),model.getIndicatorCode(),model.getType()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("执行插入语句失败");
			return result;
		}
		return result;
	}
	/*
	 * 修改入海污染源设置
	 */
	public Result savePolluSet(PollutantModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.UPDATE);
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		result.setMessage("修改成功");
		//唯一性检查
		StringBuffer checkSql = new StringBuffer("");
		checkSql.append(" select count(1) from aiot_pollutant_indicator");
		checkSql.append(" where wpid = ").append(model.getWpId());
		checkSql.append(" and deviceid = ").append(model.getDeviceId());
		checkSql.append(" and indicatorCode = '").append(model.getIndicatorCode()).append("'");
		checkSql.append(" and id <> ").append(model.getId());
		int check = 0;
		try {
			check = this.queryForInt(checkSql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("唯一性检查时失败");
			return result;
		}
		if(check>0){
			result.setResult(Result.FAILED);
			result.setMessage("违反唯一性原则");
			return result;
		}
		//执行插入语句
		StringBuffer sql = new StringBuffer("");
		sql.append(" insert into aiot_pollutant_indicator(wpid,deviceid,indicatorcode,type)");
		sql.append(" values(?,?,?,?)  on duplicate key update type = values(type)");
		Object[] params = new Object[]{
				model.getWpId(),model.getDeviceId(),model.getIndicatorCode(),model.getType()
		};
		int res = 0;
		try {
			res = this.update(sql.toString(), params);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("执行插入语句失败");
			return result;
		}
		return result;
	}
	/*
	 * 删除入海污染源设置
	 */
	public Result delePolluSet(PollutantModel model){
		//初始化返回结果
		Result result = new Result();
		result.setDotype(Result.DELETE);
		result.setMessage("删除成功");
		result.setModel(JsonUtil.toJson(model));
		result.setResult(Result.SUCCESS);
		
		//执行删除操作
		StringBuffer sql = new StringBuffer("");
		sql.append(" delete from aiot_pollutant_indicator where id = ").append(model.getId());
		int res = 0;
		try {
			res = this.update(sql.toString(), null);
		} catch (Exception e) {
			result.setResult(Result.FAILED);
			result.setMessage("删除失败,请重试");
		}
		return result;
	}
	/*
	 * 获得站点需要展示的入海污染量的信息
	 */
	public List<DeviceModel> getDevices4Pollu(StationModel station){
		List<DeviceModel> list = new ArrayList<>();
		StringBuffer dsql = new StringBuffer("");
		dsql.append(" select distinct b.id,b.name,b.code");
		dsql.append(" from aiot_pollutant_indicator a,device_catalog b");
		dsql.append(" where a.deviceid = b.id");
		dsql.append(" and a.wpid =").append(station.getId());
		dsql.append(" and a.type = 1");
		list = this.queryObjectList(dsql.toString(), DeviceModel.class);
		for(DeviceModel device:list){
			StringBuffer isql = new StringBuffer("");
			List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();
			isql.append(" select distinct b.id,b.code,b.title,b.groupid,b.unitid,b.isactive");
			isql.append(" from aiot_pollutant_indicator a,dm_indicator b");
			isql.append(" where a.type = 1 and a.wpid =").append(station.getId());
			isql.append(" and a.deviceid =").append(device.getId());
			isql.append(" and a.indicatorcode = b.code");
			isql.append(" and b.isactive = 1");
			indicators = this.queryObjectList(isql.toString(), IndicatorModel.class);
			device.setIndicators(indicators);
		}
		return list;
	}
	
}
