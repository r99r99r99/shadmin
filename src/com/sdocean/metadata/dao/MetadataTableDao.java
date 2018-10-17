package com.sdocean.metadata.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.common.model.Result;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.metadata.model.HalfHour;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.station.model.StationModel;

@Component
public class MetadataTableDao extends OracleEngine{
	
	/*
	 * 通过起止时间获得需要查询的表名的集合
	 */
	public List<MetadataTable> getTables4Meta(StationModel station,String beginTime,String endTime,int type){
		List<MetadataTable> tables = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.type,a.tablename,a.wpid,a.begintime,a.endtime,a.isactive");
		sql.append(" from aiot_meta_table a");
		sql.append(" where a.begintime <= '").append(endTime).append("' and a.endtime > '").append(beginTime).append("'");
		sql.append(" and isactive = 1 and type = ").append(type);
		sql.append(" and wpid = ").append(station.getId());
		tables = this.queryObjectList(sql.toString(), MetadataTable.class);
		return tables;
	}
	 
	/*
	 * 通过站点,以及查询时间\类型,得到需要查询的元数据表
	 */
	public MetadataTable getOneTable(StationModel station,String collect_time,int type){
		MetadataTable table = new MetadataTable();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.type,a.tablename,a.wpid,a.begintime,a.endtime,a.isactive from aiot_meta_table a");
		sql.append(" where wpid =").append(station.getId());
		sql.append(" and begintime <= '").append(collect_time).append("'");
		sql.append(" and endtime > '").append(collect_time).append("'");
		sql.append(" and type = ").append(type);
		sql.append(" and isactive = 1");
		table = this.queryObject(sql.toString(), MetadataTable.class);
		return table;
	}
	
	/*
	 * 获得某站点最后的元数据表
	 */
	public MetadataTable getLastTable4Station(StationModel model,int type){
		StringBuffer lastMetaSql = new StringBuffer("");
		lastMetaSql.append(" select id,type,tablename,wpid,begintime,endtime,isactive from aiot_meta_table where isactive = 1 and wpid =").append(model.getId());
		lastMetaSql.append(" and type = ").append(type);
		lastMetaSql.append(" order by endtime desc limit 1");
		MetadataTable lastmetaTable = this.queryObject(lastMetaSql.toString(), MetadataTable.class);
		return lastmetaTable;
	}
	
	//通过上个元数据表,获得下个元数据表
	public MetadataTable getNextMetaTable(MetadataTable lastmetaTable,String metaName,StationModel station,int type){
		 //定义时间格式
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MetadataTable nowMetaTable = new MetadataTable();
		//获得上次的截止时间作为这次的起始时间
		String bgmeta = lastmetaTable.getEndTime();
		Date nDate = null;
		try {
			nDate = df.parse(bgmeta);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(nDate);
	    //获得一个季度后(三个月)的时间
	    calendar.add(calendar.MONTH, 3);
	    String edmeta = df.format(calendar.getTime()).substring(0,10)+" 00:00:00";
	    int sid = 0;
	    //除了辽河站点外,其他站点中间为自己的ID
	    if(station.getId()==10001){
	    	sid = 4;
	    }else{
	    	sid = station.getId();
	    }
	    //获得起始时间的年份简写
	    int myear = nDate.getYear()-100;
	    //获得编号的简写
	    int mid = (nDate.getMonth()+3)/3;
	    metaName = metaName +"_"+sid+"_"+myear+"_"+mid;
	    nowMetaTable.setType(type);
	    nowMetaTable.setWpId(station.getId());
	    nowMetaTable.setTableName(metaName);
	    nowMetaTable.setBeginTime(bgmeta);
	    nowMetaTable.setEndTime(edmeta);
	    nowMetaTable.setIsactive(1);
	    
	    return nowMetaTable;
	}
	/*
	 * 新建元数据表
	 */
	public String saveMetaTable(MetadataTable model,int type){
		String result = "";
		//将数据存入到表中
		StringBuffer insertSql = new StringBuffer("");
		insertSql.append(" insert into aiot_meta_table(type,tablename,wpid,begintime,endtime,isactive)");
		insertSql.append(" values(?,?,?,?,?,?) on duplicate key update tablename=values(tablename)");
		Object[] insertParams = new Object[]{
				type,model.getTableName(),model.getWpId(),
				model.getBeginTime(),model.getEndTime(),model.getIsactive()
		};
		int insert = 0;
		try {
			insert = this.update(insertSql.toString(), insertParams);
		} catch (Exception e) {
			result = "保存到元数据表中失败";
			return result;
		}
		//生成元数据表
		String liketable = "";
		if(type==1){
			liketable = "aiot_metadata";
		}else if(type==2){
			liketable = "aiot_syndata";
		}
		//判断是否存在同名表
		String checkSql = "select 1 from "+model.getTableName();
		int check = 0;
		try {
			check = this.queryForInt(checkSql, null);
		} catch (Exception e) {
			check = -1;     //当check=-1时表示没有改表
		}
		if(check<0){
			StringBuffer createtable = new StringBuffer("");
			createtable.append("create table ").append(model.getTableName()).append(" like ").append(liketable);
			try {
				this.execute(createtable.toString());
			} catch (Exception e) {
				result = "生成元数据表失败";
				return result;
			}
		}
		
		return result;
	}
	
}
