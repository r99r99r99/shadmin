package com.sdocean.metadata.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.common.model.Result;
import com.sdocean.frame.util.JsonUtil;
import com.sdocean.log.dao.OperationLogDao;
import com.sdocean.log.model.OperationLogModel;
import com.sdocean.menu.model.CurrMenu;
import com.sdocean.metadata.dao.MetadataTableDao;
import com.sdocean.metadata.model.MetadataTable;
import com.sdocean.station.model.StationModel;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class MetadataTableService {
	
	@Autowired
	MetadataTableDao metaTableDao;
	
	/*
	 * 为站点添加元数据表以及综合元数据表
	 */
	public Result makeMetaTable4Station(StationModel station,int num){
		Result result = new Result();
		result.setMessage("生成成功");
		 //定义时间格式
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    //得到当前的年份
	    Date nowDate = new Date();
	    int year = nowDate.getYear()-1+1900;
	    String bgtime = year+"-10-01 00:00:00";
	    String edtime = (year+1)+"-01-01 00:00:00";
	    //获得最后的元数据表
	    MetadataTable lastmetaTable = metaTableDao.getLastTable4Station(station, 1);
	    if(lastmetaTable==null||lastmetaTable.getWpId()<10000){
			lastmetaTable = new MetadataTable();
			lastmetaTable.setType(1);
			lastmetaTable.setWpId(station.getId());
			lastmetaTable.setBeginTime(bgtime);
			lastmetaTable.setEndTime(edtime);
		}
	    //获得最后的综合元数据表
	    MetadataTable lastSynTable = metaTableDao.getLastTable4Station(station, 2);
	    if(lastSynTable==null||lastSynTable.getWpId()<10000){
			lastSynTable = new MetadataTable();
			lastSynTable.setType(2);
			lastSynTable.setWpId(station.getId());
			lastSynTable.setBeginTime(bgtime);
			lastSynTable.setEndTime(edtime);
		}
	    for(int i=0;i<num;i++){
			//元数据部分
			MetadataTable nowmetaTable = metaTableDao.getNextMetaTable(lastmetaTable, "aiot_metadata", station,1);
			lastmetaTable = nowmetaTable;
			     //将数据存入到数据库中
			    String res = metaTableDao.saveMetaTable(nowmetaTable, 1);
			    if(res.length()>0){
			    	result.setMessage(res);
			    	return result;
			    }
			
			//综合元数据部分
			MetadataTable nowsynTable = metaTableDao.getNextMetaTable(lastSynTable, "aiot_syndata", station,2);
			lastSynTable = nowsynTable;
				//将数据存入到数据库中
				String resu = metaTableDao.saveMetaTable(nowsynTable, 2);
				if(resu.length()>0){
					result.setMessage(resu);
					return result;
				}
		}
	    
		return result;
	}
	
	
	

}
