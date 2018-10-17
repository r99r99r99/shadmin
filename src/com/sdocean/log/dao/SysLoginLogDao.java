package com.sdocean.log.dao; 

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sdocean.frame.dao.OracleEngine;
import com.sdocean.log.model.SysLoginLogModel;

@Component
public class SysLoginLogDao extends OracleEngine{
	
	public void saveSysLoginLog(SysLoginLogModel model){
		StringBuffer sql = new StringBuffer("");
		Object[] qaram = new Object[]{model.getUserId(),model.getLoginTime(),model.getLoginType(),
				                      model.getIpAddress(),model.getSystemCode(),model.getSystemName()};
		sql.append("insert into sys_login_log(userid,logintime,logintype,ipaddress,systemcode,systemname) values(?,?,?,?,?,?)");
		this.update(sql.toString(), new Object[]{model.getUserId(),model.getLoginTime(),model.getLoginType(),
            model.getIpAddress(),model.getSystemCode(),model.getSystemName()});
	}
	/*
	 * 查询一定时间范围内的用户登录日志
	 */
	public List<SysLoginLogModel> getLoginLogList(SysLoginLogModel model){
		List<SysLoginLogModel> list = new ArrayList<>();
		StringBuffer sql = new StringBuffer("");
		sql.append(" select a.id,a.userid,b.realname as username,a.logintime as logtime,a.logintype,c.value as logintypename,");
		sql.append(" a.ipaddress,a.systemcode,a.systemname");
		sql.append(" from sys_login_log a,sys_user b,sys_public c");
		sql.append(" where a.userid = b.id");
		sql.append(" and c.parentcode = '0001' and a.logintype = c.classid");
		//增加查询条件
		if(model!=null&&model.getBeginTime()!=null&&model.getBeginTime().length()>0){
			sql.append(" and a.logintime >= '").append(model.getBeginTime()).append("'");
		}
		if(model!=null&&model.getEndTime()!=null&&model.getEndTime().length()>0){
			sql.append(" and a.logintime <= '").append(model.getEndTime()).append("'");
		}
		//增加排序语句
		sql.append(" order by a.logintime desc");
		list = this.queryObjectList(sql.toString(), SysLoginLogModel.class);
		return list;
	}
	
}
