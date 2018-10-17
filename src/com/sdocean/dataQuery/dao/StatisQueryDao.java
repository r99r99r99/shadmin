package com.sdocean.dataQuery.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sdocean.dataQuery.model.StatData;
import com.sdocean.dataQuery.model.StatisModel;
import com.sdocean.firstpage.model.WaterStandard;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.indicator.dao.IndicatorDao;
import com.sdocean.indicator.model.IndicatorModel;
import com.sdocean.metadata.dao.MetadataTableDao;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.station.model.StationModel;

@Component
public class StatisQueryDao extends OracleEngine{
	
	@Resource
	MetadataTableDao tableDao;
	@Resource
	IndicatorDao indicatorDao;
	/*
	 * 为多个站点的水质统计提供饼形图以及统计列表数据
	 */
	public List<StatData> getData4pieByAll(StatisModel model,List<StationModel> stations,int type){
		List<StatData> list = new ArrayList<StatData>();
		//获得起止时间
		String bdate = model.getBeginDate();
		String edate = model.getEndDate();
		String groupSql = "";
		//按照统计口径
		String statType = model.getStatType()+"";
		if(statType.equals("1")){//按照每月口径统计
			groupSql = "date_format(a.collect_time,'%Y-%m')";
		}else if(statType.equals("2")){//按照每周口径统计
			groupSql = "concat(year(a.collect_time),week(a.collect_time))";
		}else if(statType.equals("3")){
			groupSql = "date_format(a.collect_time,'%Y-%m-%d')";
		}
		//遍历站点列表
		StringBuffer sql = new StringBuffer("");
		sql.append(" select m.time,m.fieldName,max(m.standard_grade) as standard_grade");
		sql.append(" from");
		sql.append(" (");
		for(int j=0;j<stations.size();j++){
		//for(StationModel station:stations){
			StationModel station = stations.get(j);
			//通过观测点以及时间得到需要查询的TABLENAME列表
			List<MetadataTable> tableList = tableDao.getTables4Meta(station,bdate, edate, type);
			//查询每个表中的数据
			for(int i=0;i<tableList.size();i++){
				MetadataTable table = tableList.get(i);
				String tableName = table.getTableName();
				sql.append("   select ").append(groupSql).append(" as time,c.title as fieldName,max(b.standard_grade) as standard_grade");
				sql.append("   from ").append(tableName).append(" a,waterqualitystandard b,dm_indicator c");
				sql.append("   where a.collect_time between '").append(bdate).append("' and '").append(edate).append("'");
				sql.append("   and a.indicator_code = b.item and a.indicator_code = c.code");
				sql.append("   and a.wpid = ").append(station.getId());
				sql.append("   and case when b.min = 1 then a.data >= b.min_value else a.data > b.min_value end");
				sql.append("   and case when b.max = 1 then a.data <= b.max_value else a.data < b.max_value end");
				
				//sql.append("   and a.data >= b.min_value and a.data < b.max_value");
				sql.append("   and b.water_type = ").append(station.getWaterType());
				sql.append("   group by ").append(groupSql).append(",a.indicator_code");
				
				if(j>=stations.size()-1&&i>=tableList.size()-1){
					
				}else{
					sql.append(" union all ");
				}
			}
			
		}
		sql.append("   order by standard_grade desc ");
		sql.append(" ) m group by m.time");
		
		//拼接SQL语句,获得当前每个统计口径的最高等级参数
		//获得总共的统计口径的个数
		StringBuffer sqlCount = new StringBuffer("");
		sqlCount.append("select count(1) from (").append(sql).append(") n");
		int statCount = 0;
		try {
			statCount = this.queryForInt(sqlCount.toString(), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
						
		//获得每个水质等级的个数以及首要污染源是什么
		StringBuffer sqlList = new StringBuffer("");
		sqlList.append(" select count(n.time) as y,group_concat(distinct(fieldName)) as firstThing,n.standard_grade,s.value as name,s.remark as color");
		sqlList.append(" from (");
		sqlList.append(sql);
		sqlList.append(" ) n , sys_public s where s.parentcode = '0007' and n.standard_grade = s.classid group by n.standard_grade");
		try {
			list = this.queryObjectList(sqlList.toString(), StatData.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将结果总数融合到list中
		if(list!=null&&list.size()>0){
			for(StatData statData:list){
				statData.setStatcount(statCount);
			}
		}	
				
		return list;
	}
	
	/*
	 * 为水质统计提供饼形图以及统计列表数据
	 */
	public List<StatData> getData4pie(StatisModel model,StationModel station,int type,List<StatData> statDatas){
		List<StatData> list = new ArrayList<StatData>();
		List<StatData> result = new ArrayList<StatData>();
		//获得该统计口径下的总数
		int statCount = 0;
		try {
			statCount = statDatas.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//统计每个水质等级的天数,首要污染因子以及占比
		//初始化每个水质等级
		StringBuffer sql = new StringBuffer("");
		sql.append(" select classid as standard_grade,value as name,remark as color,").append(statCount).append(" as statcount,0 as y,'' as firstThing");
		sql.append(" from sys_public ");
		sql.append(" where parentcode = '0007'");
		sql.append(" order by ordercode");
		list = this.queryObjectList(sql.toString(), StatData.class);
		//遍历线性数据的结果,并将结果统计到饼形图中
		for(StatData ad:statDatas){
			for(int i=0;i<list.size();i++){
				StatData li = list.get(i);
				if(Integer.parseInt(ad.getYdata())==li.getStandard_grade()){
					//更新当前水质等级的总数
					li.setY(li.getY()+1);
					//获得首要污染因子
					if(!li.getFirstThing().contains(ad.getName())){
						if(li.getFirstThing().length()>0){
							li.setFirstThing(li.getFirstThing()+",");
						}
						li.setFirstThing(li.getFirstThing()+ad.getName());
					}
				}
				
				list.set(i, li);
			}
		}
		//遍历结果,去掉没有数据的水质等级
		for(StatData li:list){
			if(li.getY()>0){
				result.add(li);
			}
		}
		
		return result;
	}
	
	/*
	 * 查询多个站点的水质统计
	 * 按照不同的统计口径
	 */
	public List<StatData> getStatDataSearchList4Stations(StatisModel gmodel,List<StationModel> stations,int type){
		List<StatData> statDatas = new ArrayList<StatData>();
		//获得起止时间
		String bdate = gmodel.getBeginDate();
		String edate = gmodel.getEndDate();
		String groupSql = "";
		//按照统计口径
		String statType = gmodel.getStatType()+"";
		if(statType.equals("1")){//按照每月口径统计
			groupSql = "date_format(a.collect_time,'%Y-%m')";
		}else if(statType.equals("2")){//按照每周口径统计
			groupSql = "concat(year(a.collect_time),week(a.collect_time))";
		}else if(statType.equals("3")){
			groupSql = "date_format(a.collect_time,'%Y-%m-%d')";
		}
		//遍历站点信息,
		for(StationModel station:stations){
			//通过观测点以及时间得到需要查询的TABLENAME列表
			List<MetadataTable> tableList = tableDao.getTables4Meta(station,bdate, edate, type);
			//拼接SQL语句,获得当前每个统计口径的最高等级参数
			StringBuffer sql = new StringBuffer("");
			//读取配置文件,得到有水质标准的参数信息
			List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();
			StringBuffer standardSql = new StringBuffer("");
			standardSql.append(" select distinct b.id as indicatorid,b.code,b.title");
			standardSql.append(" from waterqualitystandard a,dm_indicator b");
			standardSql.append(" where a.water_type = ").append(station.getWaterType());
			standardSql.append(" and a.item = b.code and b.isactive = 1");
			standardSql.append(" order by a.id desc");
			try {
				indicators = this.queryObjectList(standardSql.toString(), IndicatorModel.class);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(indicators==null||indicators.size()<1){
				continue;
			}
			
			//遍历参数信息,依次查询出在规定范围内的每个统计口径的最高水质标准
			for(IndicatorModel indicator:indicators){
				for(MetadataTable tableName:tableList){
					String tablename = tableName.getTableName();
					StringBuffer standSql = new StringBuffer("");
					standSql.append(" select ").append(groupSql).append(" as xtime,'");
					standSql.append(indicator.getTitle()).append("'  as name,max(b.standard_grade) as ydata");
					standSql.append(" from ").append(tablename).append(" a ,waterqualitystandard b");
					standSql.append(" where a.collect_time between '").append(bdate).append("' and '").append(edate).append("'");
					standSql.append(" and a.wpid = ").append(station.getId()).append(" and a.indicator_code = '").append(indicator.getCode()).append("' and a.collect_type = '1'");
					standSql.append(" and b.item = '").append(indicator.getCode()).append("'");
					standSql.append(" and case when b.min=1 then a.data >= b.min_value else a.data > b.min_value end");
					standSql.append(" and case when b.max=1 then a.data <= b.max_value else a.data < b.max_value end");
					//standSql.append(" and a.data >= b.min_value and a.data < b.max_value");
					standSql.append(" and a.data <> 88888");
					standSql.append(" and collect_type = ").append(station.getWaterType());
					standSql.append(" group by ").append(groupSql);
					standSql.append(" order by ").append(groupSql).append(" ");
					List<StatData> statlist = new ArrayList<StatData>();
					try {
						statlist = this.queryObjectList(standSql.toString(), StatData.class);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					for(int i=0;i<statlist.size();i++){
						StatData stat = statlist.get(i);
						//定义是否有匹配 初始化没有匹配
						Boolean ifnothave = true;
						for(StatData statdata:statDatas){
							if(stat.getXtime().equals(statdata.getXtime())){
								if(Integer.parseInt(stat.getYdata())>Integer.parseInt(statdata.getYdata())){
									Collections.replaceAll(statDatas, statdata, stat);
								}
								//
								ifnothave = false;
							}
						}
						//如果没有匹配,则加入数据 并生成正确的顺序
						if(ifnothave){
							//是否有符合条件
							Boolean ifcheck = false;
							//初始化要插入数据的索引
							int index = 0;
							//获得要插入的数据的时间
							String inData = stat.getXtime();
							for(int j=0;j<statDatas.size();j++){
								//获得原来数据的时间
								String stData = statDatas.get(j).getXtime();
								int inst = inData.compareTo(stData);
								if(inst<0&&!ifcheck){
									ifcheck = true;
									index = j;
								}
							}
							if(ifcheck){//如果有匹配的,则根据索引插入数据
								statDatas.add(index,stat);
							}else{ //如果没有匹配的数据,则在最后插入数据
								statDatas.add(stat);
							}
						}
					}
					
				}
			}
		}
				
				
		return statDatas;
	}
	
	/*
	 *为水质统计查询折线图 
	 */
	public List<StatData> getStatDataSearchList(StatisModel gmodel,StationModel station,int type){
		List<StatData> statDatas = new ArrayList<StatData>();
		//获得起止时间
		String bdate = gmodel.getBeginDate();
		String edate = gmodel.getEndDate();
		String groupSql = "";
		//按照统计口径
		String statType = gmodel.getStatType()+"";
		if(statType.equals("1")){//按照每月口径统计
			groupSql = "date_format(collect_time,'%Y-%m')";
		}else if(statType.equals("2")){//按照每周口径统计
			groupSql = "concat(year(collect_time),week(collect_time))";
		}else if(statType.equals("3")){
			groupSql = "date_format(collect_time,'%Y-%m-%d')";
		}
		//通过观测点以及时间得到需要查询的TABLENAME列表
		List<MetadataTable> tableList = tableDao.getTables4Meta(station,bdate, edate, type);
		//拼接SQL语句,获得当前每个统计口径的最高等级参数
		StringBuffer sql = new StringBuffer("");
		//读取配置文件,得到有水质标准的参数信息
		List<IndicatorModel> indicators = new ArrayList<IndicatorModel>();
		StringBuffer standardSql = new StringBuffer("");
		standardSql.append(" select distinct b.id as indicatorid,b.code,b.title");
		standardSql.append(" from waterqualitystandard a,dm_indicator b");
		standardSql.append(" where a.water_type = ").append(station.getWaterType());
		standardSql.append(" and a.item = b.code and b.isactive = 1");
		standardSql.append(" order by a.id desc");
		try {
			indicators = this.queryObjectList(standardSql.toString(), IndicatorModel.class);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(indicators==null||indicators.size()<1){
			return null;
		}
		//遍历参数信息,依次查询出在规定范围内的每个统计口径的最高水质标准
		for(IndicatorModel indicator:indicators){
			for(MetadataTable tableName:tableList){
				String tablename = tableName.getTableName();
				StringBuffer standSql = new StringBuffer("");
				standSql.append(" select a.collect_time as xtime,'");
				standSql.append(indicator.getTitle()).append("'  as name,b.standard_grade as ydata");
				standSql.append(" from (");
				standSql.append(" select ").append(groupSql).append(" as collect_time,avg(data) as data");
				standSql.append(" from ").append(tablename);
				standSql.append(" where collect_time between '").append(bdate).append("' and '").append(edate).append("'");
				standSql.append(" and wpid = ").append(station.getId()).append(" and indicator_code = '").append(indicator.getCode()).append("' and collect_type = '1'");
				standSql.append(" and data <> 88888");
				standSql.append(" and (indicator_code <> 'Oil' or collect_time >='2017-08-09')");
				standSql.append(" group by ").append(groupSql).append(") a,waterqualitystandard b");
				standSql.append(" where b.item = '").append(indicator.getCode()).append("'");
				standSql.append(" and b.water_type = ").append(station.getWaterType());
				standSql.append(" and case when b.min=1 then a.data >= b.min_value else a.data > b.min_value end");
				standSql.append(" and case when b.max=1 then a.data <= b.max_value else a.data < b.max_value end");
				standSql.append(" order by ").append(groupSql).append(" ");
				List<StatData> statlist = new ArrayList<StatData>();
				try {
					statlist = this.queryObjectList(standSql.toString(), StatData.class);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int i=0;i<statlist.size();i++){
					StatData stat = statlist.get(i);
					//定义是否有匹配 初始化没有匹配
					Boolean ifnothave = true;
					for(StatData statdata:statDatas){
						if(stat.getXtime().equals(statdata.getXtime())){
							if(Integer.parseInt(stat.getYdata())>Integer.parseInt(statdata.getYdata())){
								Collections.replaceAll(statDatas, statdata, stat);
							}
							//
							ifnothave = false;
						}
					}
					//如果没有匹配,则加入数据 并生成正确的顺序
					if(ifnothave){
						//是否有符合条件
						Boolean ifcheck = false;
						//初始化要插入数据的索引
						int index = 0;
						//获得要插入的数据的时间
						String inData = stat.getXtime();
						for(int j=0;j<statDatas.size();j++){
							//获得原来数据的时间
							String stData = statDatas.get(j).getXtime();
							int inst = inData.compareTo(stData);
							if(inst<0&&!ifcheck){
								ifcheck = true;
								index = j;
							}
						}
						if(ifcheck){//如果有匹配的,则根据索引插入数据
							statDatas.add(index,stat);
						}else{ //如果没有匹配的数据,则在最后插入数据
							statDatas.add(stat);
						}
					}
				}
				
			}
		}
		return statDatas;
	}
	/*
	 * 为首页提供多个站点的试试水质统计
	 */
	public WaterStandard getWaterStandard4All(List<StationModel> stations){
		WaterStandard waterStandard = new WaterStandard();
		//获得当前的时间,
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String nowDate = df.format(new Date());
		//开始拼接SQL
		StringBuffer wsql = new StringBuffer("");
		wsql.append(" select m.standard_grade,m.standard_name as standardName,m.sdata,m.indicator_code as indicatorcode,m.indicator_name as indicatorName,m.unit_name as unit ");
		wsql.append(" from ( ");
		wsql.append(" select max(k.collect_time),k.indicator_code,k.standard_grade,k.standard_name,");
		wsql.append(" k.sdata,k.indicator_name,k.unit_name");
		wsql.append(" from (");
		for(int i=0;i<stations.size();i++){
			//判断应该查询的表
			StationModel station = stations.get(i);
			MetadataTable table = tableDao.getOneTable(station, nowDate, 1);
			String tableName = table.getTableName();
			
			wsql.append(" select a.collect_time ,b.standard_grade,s.value as standard_name,");
			wsql.append(" a.data as sdata,d.code as indicator_code,d.title as indicator_name,e.logo as unit_name");
			wsql.append(" from ").append(tableName).append(" a,waterqualitystandard b,dm_indicator d,g_unit e,sys_public s");
			wsql.append(" where a.indicator_code = b.item and b.item = d.code and d.unitid = e.id");
			wsql.append(" and a.data <> 88888");
			wsql.append(" and a.wpid =").append(station.getId()).append(" and s.parentcode = '0007'");
			wsql.append(" and b.standard_grade = s.classid ");
			wsql.append(" and case when b.min=1 then a.data >= b.min_value else a.data > b.min_value end");
			wsql.append(" and case when b.max=1 then a.data <= b.max_value else a.data < b.max_value end");
			//wsql.append(" and a.data >= b.min_value and a.data < b.max_value");
			//wsql.append(" and a.collect_time >= '").append(nowDate).append("'");
			wsql.append(" and b.water_type = ").append(station.getWaterType()).append(" and d.isactive = 1");
			
			if(i<stations.size()-1){
				wsql.append(" union all ");
			}
			
		}	
		wsql.append(" order by a.collect_time desc");
		wsql.append(" ) k group by k.indicator_code");
		wsql.append(" ) m order by m.standard_grade desc limit 1");
		return waterStandard;
	}
	
	/*
	 * 为首页提供水质
	 */
	public WaterStandard getWaterStandard(StationModel station){
		WaterStandard waterStandard = new WaterStandard();
		//获得当前的时间,并判断应该查询的表
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		String nowDate = df.format(new Date());
		MetadataTable table = tableDao.getOneTable(station, nowDate, 1);
		String tableName = table.getTableName();
		//开始拼接SQL
		StringBuffer wsql = new StringBuffer("");
		wsql.append(" select m.standard_grade,m.standard_name as standardName,m.sdata,m.indicator_code as indicatorcode,m.indicator_name as indicatorName,m.unit_name as unit ");
		wsql.append(" from ( ");
		wsql.append(" select max(k.collect_time),k.indicator_code,k.standard_grade,k.standard_name,");
		wsql.append(" k.sdata,k.indicator_name,k.unit_name");
		wsql.append(" from (");
		wsql.append(" select a.collect_time ,b.standard_grade,s.classname as standard_name,");
		wsql.append(" a.data as sdata,d.code as indicator_code,d.title as indicator_name,e.logo as unit_name");
		wsql.append(" from ").append(tableName).append(" a,waterqualitystandard b,dm_indicator d,g_unit e,g_waterstandard_config s");
		wsql.append(" where a.indicator_code = b.item and b.item = d.code and d.unitid = e.id");
		wsql.append(" and a.data <> 88888");
		wsql.append(" and a.wpid =").append(station.getId()).append(" and s.typeid = ").append(station.getWaterType());
		wsql.append(" and b.standard_grade = s.classid ");
		wsql.append(" and case when b.min=1 then a.data >= b.min_value else a.data > b.min_value end");
		wsql.append(" and case when b.max=1 then a.data <= b.max_value else a.data < b.max_value end");
		//wsql.append(" and a.data >= b.min_value and a.data < b.max_value");
		//wsql.append(" and a.collect_time >= '").append(nowDate).append("'");
		wsql.append(" and b.water_type = ").append(station.getWaterType()).append(" and d.isactive = 1");
		wsql.append(" order by a.collect_time desc");
		wsql.append(" ) k group by k.indicator_code");
		wsql.append(" ) m order by m.standard_grade desc limit 1");
		waterStandard = this.queryObject(wsql.toString(), WaterStandard.class);
		StringBuffer levelValue = new StringBuffer("");;
		if(waterStandard.getStandard_grade()<=station.getStandard()){
			levelValue.append(station.getTitle()).append("符合").append(station.getWaterTypeName()).append(station.getStandardName()).append("标准");
		}else if(waterStandard.getStandard_grade()>station.getStandard()){
			levelValue.append(station.getTitle()).append("超过").append(station.getWaterTypeName()).append(station.getStandardName()).append("标准,");
			levelValue.append("首要因子为:").append(waterStandard.getIndicatorName());
		}
		waterStandard.setLevelValue(levelValue.toString());
		
		return waterStandard;
	}
	
}
