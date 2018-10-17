package com.sdocean.log.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sdocean.device.model.DeviceModel;
import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.log.model.OperationLogModel;
import com.sdocean.log.model.SysLog;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.page.model.UiColumn;

@Component
public class OperationLogDao extends OracleEngine{
	
	/*public void saveSysLoginLog(SysLoginLogModel model){
		StringBuffer sql = new StringBuffer("");
		Object[] qaram = new Object[]{model.getUserId(),model.getLoginTime(),model.getLoginType(),
				                      model.getIpAddress(),model.getSystemCode(),model.getSystemName()};
		sql.append("insert into sys_login_log(userid,logintime,logintype,ipaddress,systemcode,systemname) values(?,?,?,?,?,?)");
		this.update(sql.toString(), new Object[]{model.getUserId(),model.getLoginTime(),model.getLoginType(),
            model.getIpAddress(),model.getSystemCode(),model.getSystemName()});
	}*/
	
	/*
	 * 将用户的操作存入到数据库中
	 */
	public void saveOperationLog(OperationLogModel model){
		StringBuffer sql = new StringBuffer("");
		Object[] qaram = new Object[]{
				model.getUserId(),model.getMenuname(),model.getUrl(),model.getDotype(),model.getModel(),
				model.getResult(),model.getErrorcode(),model.getMessage()
		};
		sql.append(" insert into sys_opeartion_log(userid,menuname,url,dotype,model,result,errorcode,message) values(?,?,?,?,?,?,?,?)");
		this.update(sql.toString(), qaram);
	}
	
	/*
	 * 根据查询条件查询用户操作日志
	 */
	public List<OperationLogModel> getOperationLogs(OperationLogModel model){
		List<OperationLogModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.userid,b.realname as username,a.menuname,a.url,");
		sql.append(" a.dotype,c.value as dotypename,a.model,a.result,");
		sql.append(" case a.result when 1 then '成功' else '失败' end as resultName,");
		sql.append(" a.message,a.errorcode,a.opeartiontime as operaTime");
		sql.append(" from sys_opeartion_log a,sys_user b,sys_public c");
		sql.append(" where a.userid = b.id and b.isactive = 1");
		sql.append(" and a.dotype = c.classid and c.parentcode = '0011'");
		//增加查询条件
		if(model!=null&&model.getBeginTime()!=null&&model.getBeginTime().length()>0){
			sql.append(" and a.opeartiontime >='").append(model.getBeginTime()).append("'");
		}
		if(model!=null&&model.getEndTime()!=null&&model.getEndTime().length()>0){
			sql.append(" and a.opeartiontime <= '").append(model.getEndTime()).append("'");
		}
		//增加排序
		sql.append(" order by opeartiontime desc");
		list = this.queryObjectList(sql.toString(), OperationLogModel.class);
		return list;
	}
	
	/*
	 * 为系统状态日志提供表头
	 */
	public List<UiColumn> getCols4SysLogs(SysLog model){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		//添加第一排时间列
		UiColumn col1 = new UiColumn();
		//String displayName, String field, Boolean visible,String width
		col1.setDisplayName("采集时间");
		col1.setField("collect_time");
		col1.setVisible(true);
		col1.setWidth("*");
		//得到参数状态列表
		StringBuffer csql = new StringBuffer("");
		csql.append(" select b.title as displayName,b.code as field,'true' as visible,'*' as width");
		csql.append(" from aiot_firstpage_system_show a,dm_indicator b");
		csql.append(" where a.indicatorcode = b.code");
		csql.append(" and b.isactive = 1");
		if(model!=null&&model.getWpId()>0){
			csql.append(" and a.wpid = ").append(model.getWpId());
		}
		List<UiColumn> cols1 = new ArrayList<UiColumn>();
		cols1 = this.queryObjectList(csql.toString(), UiColumn.class);
		//得到设备状态列表
		StringBuffer dsql = new StringBuffer("");
		dsql.append(" select b.name as displayName,b.code as field,'true' as visible,'*' as width");
		dsql.append(" from aiot_firstpage_system_show a,device_catalog b");
		dsql.append(" where a.indicatorcode = b.code");
		if(model!=null&&model.getWpId()>0){
			dsql.append(" and a.wpid = ").append(model.getWpId());
		}
		List<UiColumn> cols2 = new ArrayList<UiColumn>();
		cols2 = this.queryObjectList(dsql.toString(), UiColumn.class);
		cols.add(col1);
		//cols.addAll(cols1);
		cols.addAll(cols2);
		return cols;
	}
	
	/*
	 * 为系统状态日志提供结果
	 */
	public List<Map<String, Object>> getRows4SysLogs(SysLog model){
		List<Map<String, Object>> rows = null;
		//获得需要读取数据的设备列表
		List<DeviceModel> devices = new ArrayList<DeviceModel>();
		StringBuffer dsql = new StringBuffer("");
		dsql.append(" select a.id,a.name,a.code,a.devicemodel,a.brief,a.detail,");
		dsql.append(" a.pointnum,a.mainnum");
		dsql.append(" from device_catalog a,aiot_firstpage_system_show b");
		dsql.append(" where a.code = b.indicatorcode");
		devices = this.queryObjectList(dsql.toString(), DeviceModel.class);
		//初始化查询sql语句
		StringBuffer sql = new StringBuffer("");
		StringBuffer selectSql = new StringBuffer("");
		StringBuffer fromSql = new StringBuffer("");
		StringBuffer whereSql = new StringBuffer("");
		StringBuffer groupSql = new StringBuffer("");
		StringBuffer orderSql = new StringBuffer("");
		
		selectSql.append(" select a.collect_time");
		for(DeviceModel device:devices){
			selectSql.append(",case sum(case when a.indicator_code = '").append(device.getCode()).append("'");
			selectSql.append(" then data else 0 end ) when 0 then '不正常' when 1 then '正常' when 3 then '网络不通' end as ").append(device.getCode());
		}
		fromSql.append(" from aiot_metadata_system a,device_catalog b");
		whereSql.append(" where a.indicator_code = b.code");
		if(model!=null&&model.getBeginTime()!=null&&model.getBeginTime().length()>0){
			whereSql.append(" and a.collect_time >='").append(model.getBeginTime()).append("'");
		}
		if(model!=null&&model.getEndTime()!=null&&model.getEndTime().length()>0){
			whereSql.append(" and a.collect_time <='").append(model.getEndTime()).append("'");
		}
		groupSql.append(" group by collect_time");
		orderSql.append(" order by collect_time desc");
		sql.append(selectSql).append(fromSql).append(whereSql).append(groupSql).append(orderSql);
		rows = this.queryForList(sql.toString());
		return rows;
	}
}
