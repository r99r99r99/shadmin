package com.sdocean.log.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdocean.frame.model.ConfigInfo;
import com.sdocean.log.dao.SysLoginLogDao;
import com.sdocean.log.model.SysLoginLogModel;
import com.sdocean.page.model.UiColumn;
import com.sdocean.users.model.SysUser;

@Service
@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
public class SysLoginLogService {
	
	@Autowired
	SysLoginLogDao loginLogDao;
	
	
	public void saveSysLoginLog(HttpServletRequest request, ConfigInfo info){
		SysLoginLogModel model = new SysLoginLogModel();
		//获得SESSION
		HttpSession session = request.getSession();
		SysUser user = (SysUser) session.getAttribute("user");
		model.setUserId(user.getId());
		model.setLoginTime(new Date());
		String ipAdd = request.getRemoteAddr();
		model.setIpAddress(ipAdd);
		model.setLoginType(1);
		model.setSystemCode(info.getSystemCode());
		model.setSystemName(info.getSystemName());
		//将系统信息保存到session中
		session.setAttribute("system", info);
		
		loginLogDao.saveSysLoginLog(model);
	}
	/*
	 * 为查询登录日志提供表头信息
	 */
	public List<UiColumn> getLoginCols4List(){
		List<UiColumn> cols = new ArrayList<UiColumn>();
		UiColumn col1 = new UiColumn("id", "id", false, "*");
		UiColumn col2 = new UiColumn("userId", "userId", false, "*");
		UiColumn col3 = new UiColumn("用户", "userName", true, "*");
		UiColumn col4 = new UiColumn("登录时间", "logTime", true, "*");
		UiColumn col5 = new UiColumn("登录IP", "ipAddress", true, "*");
		UiColumn col6 = new UiColumn("loginType", "loginType", false, "*");
		UiColumn col7 = new UiColumn("登录方式", "loginTypeName", true, "*");
		UiColumn col8 = new UiColumn("系统编码", "systemCode", true, "*");
		UiColumn col9 = new UiColumn("系统名称", "systemName", true, "*");
		cols.add(col1);
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
	 * 查询一定时间范围内的用户登录日志
	 */
	public List<SysLoginLogModel> getLoginLogList(SysLoginLogModel model){
		return loginLogDao.getLoginLogList(model);
	}
}
